FROM tomcat:9.0.0.M13
MAINTAINER Sushant Vairagade [sushantvairagade@sjsu.edu]

RUN mkdir /usr/local/readDir

ADD ./target/graphify-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/graphify.war

EXPOSE 8080

CMD ["catalina.sh", "run"]