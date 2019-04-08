FROM maven:3.6.0-jdk-8-alpine
COPY . /usr/src
WORKDIR /usr/src
RUN mvn clean install
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","target/carsecurity-web-0.0.1-SNAPSHOT.jar"]