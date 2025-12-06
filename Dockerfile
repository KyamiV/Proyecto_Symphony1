FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY Proyecto_Symphony1/ .
RUN mvn -f pom.xml clean package -DskipTests

FROM tomcat:9.0-jdk17
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

COPY conf/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8080
