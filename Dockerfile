FROM tomcat:9.0-jdk11-openjdk-slim

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your built WAR file as the primary web application
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]