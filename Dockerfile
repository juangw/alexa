FROM lambci/lambda:build-java11

COPY . /lambda
WORKDIR /lambda

RUN mvn clean install
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN ls -la target/classes

ENTRYPOINT ["java", "-jar", "app.jar"]
