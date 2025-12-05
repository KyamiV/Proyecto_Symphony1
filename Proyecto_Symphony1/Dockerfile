# Etapa 1: Compilar con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /symphony_proyecto1
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Desplegar en Tomcat
FROM tomcat:9.0-jdk17
COPY --from=build /symphony_proyecto1/target/*.war /usr/local/tomcat/webapps/ROOT.war