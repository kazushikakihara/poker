# Poker Hand Console App
Java 17 製のコマンドラインアプリケーションで、5 枚のカードからポーカー役を即座に判定します。Maven ベースでビルドでき、ローカル/CI/Windows 配布まで一貫して再現可能です。

## スクリーンショット/デモ
```console
$ mvn -q -DskipTests exec:java -Dexec.args="AS KS QS JS 10S"
Straight Flush

$ printf "2H 2D 2S 9C 9D\n" | java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp
Full House
```

## 特徴
- 標準入力またはコマンドライン引数の 5 枚表記（例: `AS KS QS JS 10S`）から役判定を実行
- 役の強さとタイブレーク情報を保持する堅牢な評価ロジック (`poker.PokerHandEvaluator`)
- Java 17 / Maven 3.x だけでビルド・テスト・実行できる軽量 CLI
- `launch4j-config.xml` とバッチスクリプトで Windows 向け exe のラップを再現可能
- JUnit 5 ベースのテスト (`mvn -q test`) で全役種をカバー

## 依存関係・対応環境
- **JDK**: 17（`pom.xml` の `maven.compiler.release` で指定）
- **ビルドツール**: Apache Maven 3.8+（`com.example:poker-hand-app:1.0-SNAPSHOT`）
- **テスト**: JUnit Jupiter 5.10.2
- **実行成果物**: `target/poker-hand-app-1.0-SNAPSHOT.jar`（メインクラス: `poker.PokerHandApp`）
- **Windows exe ラッパー**: Launch4j 3.50+（設定ファイル `launch4j-config.xml`）

## クイックスタート
1. リポジトリを取得します。
   ```bash
   git clone https://github.com/kazushikakihara/poker.git
   cd poker
   ```
2. ビルドしてテストを実行します。
   ```bash
   mvn -q clean package
   mvn -q test
   ```
3. 判定を走らせます（引数でも標準入力でも可）。
   ```bash
   java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp AS KS QS JS 10S
   # もしくは
   printf "AS KS QS JS 10S\n" | java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp
   ```

## 使い方
- 入力は **必ず 5 枚**、空白区切りで指定します。改行・スペース・タブのいずれも区切りとして解釈されます。
- 1 枚のカード表記は「ランク + スート」の順。
  - ランク: `2`〜`10`、`J`、`Q`、`K`、`A`。`10` の代わりに `T` も使用可能です（大小文字は区別しません）。
  - スート: `S`(Spades)、`H`(Hearts)、`D`(Diamonds)、`C`(Clubs)。大小文字どちらでも入力できます。
- 5 枚中に同一カードが含まれている場合や、未知のランク/スートが含まれる場合は `Invalid input` を表示して終了コード 1 で終了します。
- コマンドライン引数が 5 枚未満の場合は標準入力から 1 行を読み取ります。行全体が空・カード数が 5 枚未満/超過の場合も `Invalid input` になります。
- 出力は判定された役の英語名（例: `Straight Flush`, `One Pair`）。

## 実行例
```bash
# 良い例（引数 5 枚）
java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp 9C 9D 9H 9S 2D
# => Four of a Kind

# 良い例（標準入力 5 枚）
printf "AD 2S 3H 4C 5D\n" | java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp
# => Straight

# 悪い例（カードが 4 枚）
java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp AS KS QS JS
# => Invalid input（終了コード 1）

# 悪い例（未知のスート）
java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp AS KH QX JS 10S
# => Invalid input（終了コード 1）
```

## Windows 向け exe の作り方
1. Windows 上で Java 17 / Maven 3.x / Launch4j 3.50+ を用意します（Scoop の例: `scoop install temurin17-jdk maven launch4j`）。
2. JAR を生成します。
   ```powershell
   mvn -q clean package
   ```
   成功すると `target\poker-hand-app-1.0-SNAPSHOT.jar` ができます。
3. Launch4j で exe を生成します。
   ```powershell
   launch4jc.exe launch4j-config.xml
   ```
   - 出力: `dist\PokerHandApp.exe`
   - メインクラス: `poker.PokerHandApp`
   - 最低要求 JRE: 17
4. 実行はダブルクリック、またはバッチスクリプト `scripts\windows\PokerHandApp-run.cmd` を利用します。
   ```powershell
   .\dist\PokerHandApp.exe AS KS QS JS 10S
   # or
   echo "2H 2D 2S 9C 9D" | .\dist\PokerHandApp.exe
   ```

## 開発
- **プロジェクト構成**
  - `src/main/java/poker/` … メインロジック (`PokerHandApp`, `PokerHandEvaluator`, `Card`, `Rank`, `Suit`, `HandRank`, `HandResult`)
  - `src/test/java/poker/` … JUnit 5 テスト (`PokerHandEvaluatorTest`)
  - `launch4j-config.xml` … Windows exe ラップ設定
  - `scripts/windows/` … exe 実行補助スクリプト
- **ローカル実行**
  - `mvn -q -DskipTests exec:java -Dexec.args="AS KS QS JS 10S"`
  - `java -cp target/poker-hand-app-1.0-SNAPSHOT.jar poker.PokerHandApp`
- **テスト**: `mvn -q test`
- **推奨コーディングスタイル**: 4 スペースインデント・UTF-8（`.editorconfig` 参照）。
- **CI / Devcontainer**: 現時点では同梱していません。GitHub Actions で `mvn -q test` を走らせる設定を追加する場合は歓迎します。

## 貢献方法
不具合報告や改善提案は歓迎します。具体的な流れ・レビュー基準は [CONTRIBUTING.md](CONTRIBUTING.md) を参照してください。ブランチは `feat/…`・`fix/…`・`docs/…` など分かりやすい名前を付け、コミットメッセージは Conventional Commits (`feat: …`) 形式を推奨します。

## ライセンス
本プロジェクトは [MIT License](LICENSE) の下で公開されています。

## セキュリティポリシー
脆弱性報告の手順は [SECURITY.md](SECURITY.md) をご覧ください。

## 謝辞 / 関連リンク
- [Launch4j](https://launch4j.sourceforge.net/) – Windows exe ラッパー
- [Contributor Covenant](https://www.contributor-covenant.org/) – 行動規範の基礎

## バッジ
![build status](https://img.shields.io/badge/build-Maven-blue?logo=apachemaven)
![license](https://img.shields.io/badge/license-MIT-green)
![jdk](https://img.shields.io/badge/JDK-17-orange?logo=java)
