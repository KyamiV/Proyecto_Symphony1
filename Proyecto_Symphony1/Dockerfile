FROM tomcat:9.0-jdk17
# Copia tu WAR al directorio de despliegue de Tomcat
COPY target/Proyecto_Symphony1-1.0.war /usr/local/tomcat/webapps/Proyecto_Symphony1.war
EXPOSE 8080
CMD ["catalina.sh", "run"]