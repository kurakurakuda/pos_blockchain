# ベースイメージの作成
FROM node:12.16.1
# package.jsonをコピー
COPY ["package.json", "./"]
# パッケージをインストール
RUN yarn install
# ファイルを全部作業用ディレクトリにコピー
COPY . .
# コンテナを起動する際に実行されるコマンド
ENTRYPOINT [ "yarn", "start" ]