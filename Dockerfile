# Stage №1 - Build fat .jar
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage №2 - Extract layers
FROM maven:3.9.9-amazoncorretto-21-alpine AS builder
WORKDIR /builder
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} mentality.jar
RUN java -Djarmode=tools -jar mentality.jar extract --layers --destination extracted

# Stage №3 - Copy extracted layers
FROM maven:3.9.9-amazoncorretto-21-alpine
WORKDIR /app
USER spring-app:minouni
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "mentality.jar"]