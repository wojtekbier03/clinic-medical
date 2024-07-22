FROM openjdk:21-jdk
MAINTAINER wojta
COPY target/clinic-medical-0.0.1-SNAPSHOT.jar clinic-medical.jar
ENTRYPOINT ["java", "-jar", "/clinic-medical.jar"]