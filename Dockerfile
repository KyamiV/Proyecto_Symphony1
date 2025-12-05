# Etapa 1: Compilar con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY Proyecto_Symphony1/Proyecto_Symphony1 .   # copia carpeta completa con pom.xml y src
RUN mvn clean package -DskipTests

# Etapa 2: Usar Tomcat y desplegar el WAR
FROM tomcat:9.0-jdk17
COPY --from=build /app/target/Proyecto_Symphony1.war /usr/local/tomcat/webapps/Proyecto_Symphony1.war