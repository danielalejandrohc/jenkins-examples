FROM openjdk:8-jre
VOLUME /tmp
COPY /target/*.jar application-service.jar
CMD ["bash", "exec java $JAVA_OPTS -jar /application-service.jar"]