FROM openjdk:12
ADD target/investment-service-ms-0.0.1-SNAPSHOT.jar investment-service-ms-0.0.1-SNAPSHOT.jar
RUN mkdir export
EXPOSE 8069
ENTRYPOINT ["java", "-jar", "investment-service-ms-0.0.1-SNAPSHOT.jar"]
LABEL prune=true
