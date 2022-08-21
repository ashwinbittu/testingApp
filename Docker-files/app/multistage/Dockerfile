FROM openjdk:8 AS BUILD_IMAGE

RUN apt update && apt install maven -y
RUN git clone -b docker https://github.com/ashwinbittu/testingApp.git
RUN cd testingApp
RUN ls -rtl
RUN mvn install
RUN ls -rtl
RUN cp target/radammcorp-v2.war /tmp
RUN ls -rtl /tmp


FROM tomcat:8-jre11

RUN ls -rtl /tmp

RUN rm -rf /usr/local/tomcat/webapps/*


COPY --from=BUILD_IMAGE /tmp/radammcorp-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
