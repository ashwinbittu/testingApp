FROM openjdk:8 AS BUILD_IMAGE
RUN apt update && apt install maven -y
RUN git clone -b docker https://github.com/ashwinbittu/testingApp.git
RUN cd testingApp && mvn install

FROM tomcat:8-jre11

RUN rm -rf /usr/local/tomcat/webapps/*
RUN ls -rtl
COPY --from=BUILD_IMAGE target/radammcorp-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
