FROM lambci/lambda:build-java11

COPY . /lambda
WORKDIR /lambda

RUN mvn clean install
ARG JAR_FILE=target/alexa-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# ENTRYPOINT ["java", "-jar", "app.jar"]
