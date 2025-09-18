# Poker Hand Console App (Java 17)
Java 17 と Maven を使って 5 枚のカードからポーカー役を判定するコンソールアプリです。GitHub Codespaces 上での起動からビルド・テスト・実行までを迷わず再現できる手順をまとめています。

## Quickstart (GitHub Codespaces)

このプロジェクトは Java 17 / Maven を使用します。Codespaces 既定イメージには SDKMAN! が含まれます。

```bash
# バージョン確認
java -version
mvn -v

# 必要なら（初回のみ）
sdk env install || true
sdk env || true
```

### Build

```bash
mvn -q clean package -DskipTests
mvn -q test
```

### Run (標準入力で5枚を1行)

```bash
printf "AS KS QS JS TS\n" | mvn -q -DskipTests exec:java
```

### Run (コマンド引数で5枚)

```bash
mvn -q -DskipTests exec:java -Dexec.args="AS KS QS JS TS"
```

### 代表的な例

```bash
# Straight Flush
printf "AS KS QS JS TS\n" | mvn -q -DskipTests exec:java

# Full House
printf "2H 2D 2S 9C 9D\n" | mvn -q -DskipTests exec:java

# 引数版
mvn -q -DskipTests exec:java -Dexec.args="2H 2D 2S 9C 9D"
```

### Troubleshooting

* `ClassNotFoundException` の場合：パッケージとクラス名が `poker.PokerHandApp` であることを確認。
* `release version 17 not supported` の場合：`java -version` が 17 になっているか確認。`.sdkmanrc` を作成済みなら `sdk env` を実行。

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

## Windows で exe ファイルを作る手順

Launch4j を使って本プロジェクトを Windows ネイティブ実行形式（`.exe`）に変換する手順です。既存の Maven ビルド成果物（JAR）をラッパーで包む形になるため、アプリ本体のコード変更は不要です。

### 前提条件

以下のソフトウェアを Windows マシンにインストールし、環境変数から利用できるようにしておきます。

- **Java 17 JDK**: `java -version` で 17 系が表示されること。
- **Git**: プロジェクトの取得に使用します。
- **Apache Maven 3.x**: JAR をビルドするために使用します。
- **Launch4j**: JAR を exe にラップするツール。公式配布の zip を任意のフォルダ（例: `C:\tools\launch4j`）へ展開します。

### プロジェクト取得とビルド

PowerShell を管理者権限なしで開き、作業ディレクトリを用意してからリポジトリを取得・ビルドします。

```powershell
# 任意の作業ディレクトリへ移動
Set-Location $env:USERPROFILE\source

# リポジトリを取得（必要に応じて URL を変更）
git clone https://github.com/OWNER/poker.git
Set-Location .\poker

# 依存関係を解決し、JAR を生成
mvn -q clean package
```

ビルドに成功すると `target\poker-hand-app-1.0-SNAPSHOT.jar` が生成されます。以降の Launch4j 設定ではこのパスを参照します。

### Launch4j 設定ファイルの作成

Launch4j は GUI でも CLI でも設定可能ですが、自動化しやすい XML 設定ファイルを作るのがおすすめです。例として `launch4j-config.xml` をプロジェクト直下に作成します。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<launch4jConfig>
  <dontWrapJar>false</dontWrapJar>
  <headerType>gui</headerType>
  <outfile>${PROJECT_DIR}\dist\PokerHandApp.exe</outfile>
  <jar>${PROJECT_DIR}\target\poker-hand-app-1.0-SNAPSHOT.jar</jar>
  <errTitle>Poker Hand App</errTitle>
  <cmdLine></cmdLine>
  <chdir>.</chdir>
  <priority>normal</priority>
  <downloadUrl>https://adoptium.net/</downloadUrl>
  <supportUrl>https://github.com/OWNER/poker</supportUrl>
  <stayAlive>false</stayAlive>
  <manifest></manifest>
  <icon></icon>
  <jre>
    <path></path>
    <minVersion>17</minVersion>
    <maxVersion></maxVersion>
    <jdkPreference>preferJre</jdkPreference>
    <runtimeBits>64/32</runtimeBits>
    <bundledJrePath></bundledJrePath>
  </jre>
</launch4jConfig>
```

- `${PROJECT_DIR}` はプロジェクトの絶対パス（例: `C:\Users\you\source\poker`）に読み替えてください。
- `<headerType>` を `gui` にすることでコンソールを開かずに実行できます（必要なら `console` に変更してください）。
- `<runtimeBits>` を `64/32` にすると 64bit/32bit どちらの JRE でも起動する exe が作成されます。
- `<icon>` に `.ico` ファイルのパスを指定するとアプリ固有のアイコンを付与できます。

### Launch4j で exe を生成

作成した設定ファイルを Launch4j の CLI で処理します。以下では Launch4j を `C:\tools\launch4j` に展開した想定です。

```powershell
# Launch4j ディレクトリへ移動
Set-Location C:\tools\launch4j

# 設定ファイルを指定して exe を生成
.\launch4jc.exe C:\Users\you\source\poker\launch4j-config.xml
```

`BUILD SUCCESSFUL` と表示され、設定した `dist\PokerHandApp.exe` が生成されれば成功です。`dist` フォルダが存在しない場合は事前に `New-Item -ItemType Directory dist` などで作成しておきます。

### exe の起動

生成された `PokerHandApp.exe` をダブルクリックすると、JAR と同じポーカー役判定アプリが起動します（コンソール版として実行する設定の場合はターミナルが開きます）。配布時は同じディレクトリに必要な設定ファイルや README を同梱してください。

## ライセンス / 著者
- ライセンス情報が設定されている場合はリポジトリ直下の `LICENSE` ファイルを参照してください。未設定の場合はプロジェクト方針に合わせて追加してください。
- 著者・メンテナはリポジトリ管理者が適宜記載してください。
