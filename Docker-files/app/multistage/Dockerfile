FROM openjdk:8 AS BUILD_IMAGE
WORKDIR /tmp
RUN apt update && apt install maven -y
RUN git clone -b docker https://github.com/ashwinbittu/testingApp.git
RUN cd testingApp && mvn install
RUN ls -rtl /tmp


FROM tomcat:8-jre11
WORKDIR /tmp
RUN ls -rtl

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=BUILD_IMAGE /testingApp/target/radammcorp-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
