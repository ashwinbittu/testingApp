FROM openjdk:8 AS BUILD_IMAGE
WORKDIR /tmp
RUN apt update && apt install maven -y
RUN git clone -b docker https://github.com/ashwinbittu/testingApp.git
RUN cd testingApp && mvn install
RUN ls -rtl /tmp


FROM tomcat:8-jre11
WORKDIR /tmp


RUN rm -rf /usr/local/tomcat/webapps/*

RUN ls -rtl
RUN ls -rtl /var/lib/jenkins/workspace/testpipeline/target
COPY --from=BUILD_IMAGE /var/lib/jenkins/workspace/testpipeline/target/radammcorp-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
