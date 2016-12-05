FROM tomcat:9.0
MAINTAINER Sushant Vairagade [sushantvairagade@sjsu.edu]

ADD ./target/graphify-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/graphify.war

EXPOSE 8080

CMD ["catalina.sh", "run"]