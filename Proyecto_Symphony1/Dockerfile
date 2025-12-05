FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .   # ya estás en la carpeta con pom.xml
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17
COPY --from=build /app/target/Proyecto_Symphony1.war /usr/local/tomcat/webapps/Proyecto_Symphony1.war