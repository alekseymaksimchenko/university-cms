FROM maven:3.8.6-jdk-8 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-slim
COPY --from=build /target/university-cms-0.0.1-SNAPSHOT.jar university-cms.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","university-cms.jar"]
