FROM lambci/lambda:build-java11

COPY . /lambda
WORKDIR /lambda

RUN ./gradlew build --warning-mode=all
ARG ZIP_FILE=build/distributions/alexa.zip
COPY ${ZIP_FILE} app.zip
