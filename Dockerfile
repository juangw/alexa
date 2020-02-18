FROM lambci/lambda:build-java11

COPY . /lambda
WORKDIR /lambda

RUN gradle build
ARG JAR_FILE=build/libs/alexa-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# ENTRYPOINT ["java", "-jar", "app.jar"]
