FROM tomcat:10.1-jdk17-temurin

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your built WAR file as the primary web application
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Disable shutdown port and bind to dynamic PORT
ENV PORT=8080
RUN sed -i 's/port="8080"/port="${port.http}"/g' /usr/local/tomcat/conf/server.xml \
    && sed -i 's/<Server port="8005"/<Server port="-1"/g' /usr/local/tomcat/conf/server.xml

EXPOSE 8080

CMD ["sh", "-c", "export JAVA_OPTS=\"$JAVA_OPTS -Dport.http=${PORT:-8080}\" && catalina.sh run"]