FROM maven:3.9.9-eclipse-temurin-21 as build

COPY . .

ARG ACTIVE_PROFILE

RUN mvn clean package -P $ACTIVE_PROFILE

FROM eclipse-temurin:21

ARG JAR_FILE=/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]