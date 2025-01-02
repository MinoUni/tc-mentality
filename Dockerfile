# Stage №1 - Build fat .jar
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY --chown=spring-app:minouni . /app
RUN mvn clean package -DskipTests

# Stage №2 - Extract layers
FROM maven:3.9.9-amazoncorretto-21-alpine AS builder
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} mentality.jar
RUN java -Djarmode=tools -jar mentality.jar extract --layers --launcher

# Stage №3 - Copy extracted layers
FROM maven:3.9.9-amazoncorretto-21-alpine
USER spring-app:minouni
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]