FROM tomcat:10.1-jdk21-openjdk-slim

# Set Tomcat HTTP port to 10000 via environment variable
ENV PORT=10000
ENV CATALINA_OPTS="-Dbio.http.port=10000 -Dhttp.port=10000 -Dport.http=10000"

# Disable the shutdown port (port 8005) completely so Render cannot hit it
RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/g' /usr/local/tomcat/conf/server.xml

# Direct port 8080 to 10000 explicitly in server.xml
RUN sed -i 's/8080/10000/g' /usr/local/tomcat/conf/server.xml

# Clean default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your packaged WAR
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 10000
CMD ["catalina.sh", "run"]