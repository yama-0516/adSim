# Tomcatの公式イメージを使用
FROM tomcat:9.0-jdk17-temurin

# WARファイルをTomcatのwebappsディレクトリにコピー
COPY adSim.war /usr/local/tomcat/webapps/

# ポート8080を公開
EXPOSE 8080