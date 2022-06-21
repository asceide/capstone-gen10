FROM maven:3.8.5-eclipse-temurin-17 as builder
RUN apt -y update
WORKDIR /home/app
COPY . .
RUN mvn clean package -DskipTests -f pom.xml


FROM maven:3.8.5-eclipse-temurin-17

EXPOSE 8080/tcp
WORKDIR /home/app
COPY --from=builder /home/app/target/hiking_app_api-3.0.jar app.jar
CMD ["java", "-jar", "app.jar"]