# 公式のTomcatイメージをベースにする（Java 21対応）
FROM tomcat:10-jdk21

# WARファイルをTomcatのwebappsディレクトリにコピー
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

# 必要に応じてポートを公開（RenderなどPaaSの場合は特に8080）
EXPOSE 8080

# Tomcatを起動
CMD ["catalina.sh", "run"]