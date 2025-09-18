# Contributing Guide

ご興味を持っていただきありがとうございます！このドキュメントでは、Issue の立て方から Pull Request を送るまでの流れ、テスト・コードスタイルの基準をまとめています。

## 行動規範
参加される前に [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) を必ずお読みください。ポジティブで安全なコミュニティづくりにご協力をお願いします。

## 相談・Issue の起票
- バグ報告・機能要望・改善提案はいずれも GitHub Issues にて受け付けています。
- 再現手順、期待結果と実際の結果、使用環境（Java / Maven のバージョン）を明記してください。
- 仕様変更や大きなリファクタリングは、事前に Issue やディスカッションで方向性をすり合わせてから実装を進めてください。

## 開発環境のセットアップ
1. このリポジトリをフォークし、ローカルにクローンします。
   ```bash
   git clone https://github.com/<your-account>/poker.git
   cd poker
   ```
2. Java 17 と Maven 3.8 以上がインストールされていることを確認します。
   ```bash
   java -version
   mvn -v
   ```
3. 依存関係を取得し、テストが通ることを確認します。
   ```bash
   mvn -q clean package
   mvn -q test
   ```

## ブランチ運用
- メインブランチに直接 push しないでください。
- フィーチャーごとにブランチを作成します。例:
  - `feat/add-hand-comparison`
  - `fix/duplicate-card-validation`
  - `docs/update-readme`

## コミットメッセージ
- [Conventional Commits](https://www.conventionalcommits.org/ja/v1.0.0/) を推奨します。
  - 例: `feat: add CLI flag to compare two hands`
  - 例: `fix: handle lowercase suit symbols`
- 1 つのコミットには 1 つの論理的変更を収めるよう心掛けてください。

## コードスタイル
- Java ファイルは 4 スペースインデント、UTF-8 エンコーディング（`.editorconfig` 参照）。
- import の順序やフォーマッターは IDE 既定の Java スタイル（Google Style / IntelliJ Default など）を使用してください。自動整形を有効にすることを推奨します。
- 新しい公開 API を追加する場合は Javadoc を付与し、例外の挙動や入力フォーマットを明記してください。

## テスト
- 変更を加えた場合は `mvn -q test` で既存テストが通ることを確認してください。
- バグ修正や機能追加では、再現ケースをカバーするテストを追加してください。
- Launch4j 設定やスクリプトを変更した際は、可能な範囲で Windows 環境での動作確認結果を PR 説明に添えてください。

## Pull Request の流れ
1. フィーチャーブランチで変更を加え、必要なテストを追加します。
2. `mvn -q test` が通ることを確認します。
3. 変更内容とテスト結果を PR 説明にまとめ、関連 Issue があればリンクします。
4. レビューコメントには建設的に対応し、必要であればコミットを追加してください（rebase/force push は避け、追加コミットでの対応を推奨）。
5. レビューが通ったらメンテナがマージします。セルフマージは行わないでください。

## リリースノート / 変更履歴
- リリース時には [CHANGELOG.md](CHANGELOG.md) を更新します。
- 変更に互換性影響がある場合は `Added` / `Changed` / `Deprecated` / `Removed` / `Fixed` / `Security` など適切なセクションに追記してください。

ご協力に感謝します！楽しんでコントリビュートしてください。
