# Tomcatの公式イメージを使用
FROM tomcat:9.0-jdk17-temurin

# WARファイルをTomcatのwebappsディレクトリにコピー
COPY adSim.war /usr/local/tomcat/webapps/

# TomcatのHTTPポートを8081に変更
RUN sed -i 's/port="8080"/port="8081"/' /usr/local/tomcat/conf/server.xml

# ポート8081を公開
EXPOSE 8081