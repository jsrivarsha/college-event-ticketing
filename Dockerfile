FROM tomcat:10.1-jdk21-openjdk-slim

RUN rm -rf /usr/local/tomcat/webapps/*

COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

ENV PORT=10000
EXPOSE 10000

CMD ["catalina.sh", "run"]