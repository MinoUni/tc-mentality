# Stage №1 - Build fat .jar
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage №2 - Extract layers
FROM bellsoft/liberica-openjre-debian:21-cds AS builder
WORKDIR /builder
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# Stage №3 - Copy extracted layers
FROM bellsoft/liberica-openjre-debian:21-cds
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]