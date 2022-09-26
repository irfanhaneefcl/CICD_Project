FROM tomcat:8.5.40
COPY target/CICD_Project.war /usr/local/tomcat/webapps
EXPOSE 8080
CMD /usr/local/tomcat/bin/catalina.sh run
#
