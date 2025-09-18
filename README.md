# Poker Hand Console App (Java 17)
Java 17 と Maven を使って 5 枚のカードからポーカー役を判定するコンソールアプリです。GitHub Codespaces 上での起動からビルド・テスト・実行までを迷わず再現できる手順をまとめています。

## Open in GitHub Codespaces
リポジトリをフォーク済みならクリックで起動 → 下記の `OWNER/REPO` は自分のリポジトリ名に置き換えてください。
[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/OWNER/REPO?quickstart=1)

## 前提条件
- GitHub アカウントを持ち、Codespaces が利用可能であること（権限・クォータを確認）。
- このアプリのリポジトリを GitHub 上に用意していること（フォークまたは自分で作成）。
- ローカル環境での実行は任意ですが、Java 17 と Maven/Gradle があるとローカル確認も可能です。本 README は Codespaces での実行を主眼とします。

## クイックスタート（最短手順）
1. 上記バッジをクリックし、起動するブランチとして例 `main` を選択します。マシンタイプはデフォルトのままで構いません。
2. Codespaces が起動し VS Code (Web) が立ち上がるまで待機します。
3. エディタ下部のターミナルで Java/Maven (または Gradle) のバージョンを確認します。

```bash
java -version
mvn -v
# Gradle を使う場合
./gradlew -v
```

4. ビルド → テスト → 実行をまとめて実行します。

**Maven の場合（推奨）**
```bash
mvn -q -DskipTests package && mvn -q test && mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"
```
exec-maven-plugin が未設定で上記が失敗する場合は次を利用します。
```bash
mvn -q -DskipTests package && mvn -q test && java -cp target/classes PokerHandApp
```

**Gradle プロジェクトに読み替える場合**
```bash
./gradlew build && ./gradlew test && ./gradlew run
```
`application` プラグインが無い場合の代替:
```bash
./gradlew build && ./gradlew test && java -cp build/classes/java/main PokerHandApp
```

5. 役判定をすぐ試すには次のように標準入力をパイプします。
```bash
echo "AS KS QS JS 10S" | mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"
```

## 開発コンテナ（devcontainer）
リポジトリに `.devcontainer/devcontainer.json` が無い場合は、以下の推奨設定を追加すると再現性が高まります（既存の設定がある場合は読み替えてください）。

```json
{
  "name": "java-poker-hand",
  "image": "mcr.microsoft.com/devcontainers/java:1-17-bookworm",
  "features": {
    "ghcr.io/devcontainers/features/github-cli:1": {}
  },
  "customizations": {
    "vscode": {
      "extensions": [
        "vscjava.vscode-java-pack",
        "redhat.vscode-xml",
        "mhutchie.git-graph"
      ]
    }
  },
  "postCreateCommand": "mvn -q -DskipTests package"
}
```

## 実行方法（詳説）
- ターミナルで `mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"` を実行し、カードを 1 行で入力します。入力が終わったら `Ctrl+D`（macOS/Windows/Linux 共通、VS Code Web のターミナルも同じ）で EOF を送ります。
- 複数の入力を試す場合は `cat` や `echo` とパイプを組み合わせると効率的です。

```bash
# 実行 → 手入力の流れ
mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"
AS KS QS JS 10S
# ここで Ctrl+D を押して実行終了

# 複数例をまとめて流す
cat <<'CARDS' | mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"
AS KS QS JS 10S
9C 9D 9H 9S 2D
3H 3D 3S 8C 8D
CARDS
```

- 入力フォーマット: ランク（2〜10, J, Q, K, A または `T`）とスート（S, H, D, C）を組み合わせた 2〜3 文字のトークンをスペース区切りで 5 枚並べます。例: `AS KH QD JC 10S`。
- 不正な形式・重複カード・5 枚以外の枚数を入力すると `Invalid input` が標準エラーに出力され、プロセスは終了コード 1 になります。
- 代表的な役の例（`mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"` で実行した場合の想定出力）
  - `echo "AS KS QS JS 10S" | ...` → `Straight Flush`
  - `echo "9C 9D 9H 9S 2D" | ...` → `Four of a Kind`
  - `echo "3H 3D 3S 8C 8D" | ...` → `Full House`
  - `echo "2S 4S 6S 8S 10S" | ...` → `Flush`
  - `echo "AS 7D 5H 3C 2D" | ...` → `High Card`

## テストの実行
- 本プロジェクトは JUnit 5 を利用しています。Maven では `mvn -q test`、Gradle では `./gradlew test` を実行してください。
- 失敗したテストの詳細はターミナル出力の末尾および `target/surefire-reports`（Maven）または `build/test-results/test`（Gradle）で確認できます。テストクラス・メソッド名から対象のハンド判定ロジックを特定し、`src/test/java` のコードを参照するとデバッグが容易です。

## トラブルシューティング（Codespaces特有のポイント）
- **Java バージョンが 17 でない**: Codespaces のベースイメージを `mcr.microsoft.com/devcontainers/java:1-17-bookworm` のように指定した devcontainer を使います。手動で `sdk list java` や `update-alternatives` を操作するより確実です。
- **`exec:java` が動かない**: `pom.xml` に exec-maven-plugin を追加します。

  ```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.5.0</version>
        <configuration>
          <mainClass>PokerHandApp</mainClass>
        </configuration>
      </plugin>
    </plugins>
  </build>
  ```

- **`NoClassDefFoundError` などクラスが見つからない**: `java -cp target/classes PokerHandApp`（Maven）や `java -cp build/classes/java/main PokerHandApp`（Gradle）とクラスパスを明示します。
- **EOF の送り方が分からない**: VS Code Web のターミナルで `Ctrl+D` を押します（Windows 配列でも同じ）。入力途中でキャンセルしたい場合は `Ctrl+C`。
- **メモリ不足**: Codespaces 作成ダイアログでマシンタイプを一段上げるか、不要な拡張機能を削除します。必要なら `devcontainer.json` に重い処理を仕込まず、手動実行に切り替えてください。

## ローカル実行（任意）
- 事前に Java 17 と Maven または Gradle をインストールします。
- Maven での最小手順:

```bash
mvn -q -DskipTests package
mvn -q test
java -cp target/classes PokerHandApp <<'EOF'
AS KS QS JS 10S
EOF
```

- Gradle の例:

```bash
./gradlew build
./gradlew test
echo "AS 7D 5H 3C 2D" | java -cp build/classes/java/main PokerHandApp
```

- Windows PowerShell の例:

```powershell
mvn -q -DskipTests package
mvn -q test
"9C 9D 9H 9S 2D" | mvn -q -DskipTests exec:java -Dexec.mainClass="PokerHandApp"
```

## ライセンス / 著者
- ライセンス情報が設定されている場合はリポジトリ直下の `LICENSE` ファイルを参照してください。未設定の場合はプロジェクト方針に合わせて追加してください。
- 著者・メンテナはリポジトリ管理者が適宜記載してください。
