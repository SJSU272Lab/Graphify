# Graphify

#Abstract

Graph theory of storage is becoming de facto solution to today’s need of handling big-data, majority of organizations are still using the relational system mainly for one thing ‘Cost’ of migrating to Graph DB from their legacy system. If there was a tool to analyse your current DB structure and create a schema based on the analysis, it would save a lot of time and hence cost. We would like to create a tool that would provide easy to use user interface (drag and drop, buttons to add relationship), generate schema, create nodes and edges by migrate data from existing DB to new one. We would like to start with migrating MySQL to IBM Graph for two reasons, MySQL being one of the most widely used relational DB and IBM Graph as it has exposed APIs to do data and structure manipulations. This would help the organizations who wish to migrate or use graph DB.

#User Stories

1. As a user I want to be able to migrate my existing relational DB to Graph DB
2. As a user with no DB background I want to be able to create new Graph DB just by using drag and drop utilities

Flow diag:

![Version 2](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/flow_diag_v2.jpg)


#Strategy

There is no one correct one way of designing a database schema even if you follow the industry statndards. Using them doesn't ensure the most optimised design. The most important factor to keep in mind while designing DB schema should be your requirements.
Migrating relational DB to other relation DB (MySQL to Oracle) is pretty straight forward, whereas it is not the case with migrating realtional DB to Graph. Graphify provides one basic strategy defined that helps you migrate to Graph using the foreign keys defined in your relational schema. Below is the example of how it works:<br />
Consider a simple 'expense' DB schema:

![ER Diag](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/ER_Diagram.png)



Graphify fetches the DB schema from MySQL as [json](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/MySql.json), realtional DB [schema](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/create.sql) and converts to IBM Graph [schema](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/graphSchema.json)<br />
All that Graphify does in this stategy is that it considers the foreign key realtionships in the relational DB and defines them as edges in the Graph between two nodes (which are nothing but the tables in the relational DB). The strategy can be easily understood if these two files are compared. <br />
This is a default strategy that Graphify offers whereas users can define and create their own strategy by making another implementaion of the the [interface](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/DBService/src/main/java/com/graphify/db/rule/engine/Strategy.java) as per requirement. 
 
#How to deploy to localhost or Docker container
1. Build [DBService](https://github.com/SJSU272Lab/Fall16-Team12/tree/master/DBService)
2. Build [Graphify](https://github.com/SJSU272Lab/Fall16-Team12/tree/master/Graphify)<br />
   Use mvn clean install command to build these modules<br />
   PS: Before building Graphify module make sure that 'BaseUrl' in [web.xml](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/Graphify/webcontent/WEB-INF/web.xml) has the correct host and port, this should the host and port of the host machine.
3. Deploy the generated 'war' artifact to any web container or to Docker container, Graphify module already has a [Dockerfile](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/Graphify/Dockerfile), use below command to run the build docker image: <br />
   "docker build -t some-image-name:tag ." <br />
   eg. "docker build -t graphify:1.0 ."
   and to run the docker image use below command <br />
   "docker run --name app-name -d -p host-port:8080 some-image-name:tag --link some-mysql-container:mysql-service-alias" <br />
   eg. "docker run --name graphify -d -p 8080:8080  --link mysqldb:mysql"
 

PS: Make sure that the deployed application has access to the MySQL server hosting the DB and the provided user has read access to the schema you wish to convert.


#Demo and resources
[Watch demo here](https://youtu.be/PvrVJRi689w) <br />
[MySQL create script](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/create.sql) <br />
[MySQL sample dump](https://github.com/SJSU272Lab/Fall16-Team12/blob/master/docs/entity.zip) <br />

#Contributors
1. [Sushant Vairagade](https://github.com/sjsu-sushant)
2. [Nakshtra](https://github.com/nakshatra04)
3. [Sohrab Ali](https://github.com/ali-sohrab)
4. [Swathi Koduri](https://github.com/SwathiKoduri)
