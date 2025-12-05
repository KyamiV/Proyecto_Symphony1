# Etapa 1: Compilar con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/Proyecto_Symphony1/Proyecto_Symphony1
RUN mvn clean package -DskipTests

# Etapa 2: Usar Tomcat y desplegar el WAR
FROM tomcat:9.0-jdk17
COPY --from=build /app/Proyecto_Symphony1/Proyecto_Symphony1/target/Proyecto_Symphony1-1.0.war /usr/local/tomcat/webapps/Proyecto_Symphony1.war