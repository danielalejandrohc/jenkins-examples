FROM openjdk:8-jre
WORKDIR /microservice
COPY /target/*.jar application-service.jar
ENTRYPOINT ["java","-jar","/microservice/application-service.jar