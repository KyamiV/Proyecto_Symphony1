# Etapa 1: Compilar con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY Proyecto_Symphony1/Proyecto_Symphony1 ./Proyecto_Symphony1
RUN mvn -f Proyecto_Symphony1/pom.xml clean package -DskipTests

# Etapa 2: Desplegar en Tomcat
FROM tomcat:9.0-jdk17
COPY --from=build /app/Proyecto_Symphony1/target/Proyecto_Symphony1.war /usr/local/tomcat/webapps/ROOT.war