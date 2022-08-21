FROM tomcat:8-jre11
LABEL "Project"="testingApp"
LABEL "Author"="ashwinbittu"

RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/radammcorp-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
WORKDIR /usr/local/tomcat/
VOLUME /usr/local/tomcat/webapps