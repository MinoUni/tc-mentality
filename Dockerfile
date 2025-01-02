# Stage №1 - Build fat .jar
FROM maven:3.9.9-amazoncorretto-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

# Stage №2 - Extract layers
FROM maven:3.9.9-amazoncorretto-21-alpine AS builder
COPY --from=build /target/*.jar mentality.jar
RUN java -Djarmode=tools -jar mentality.jar extract --layers --launcher

# Stage №3 - Copy extracted layers
FROM maven:3.9.9-amazoncorretto-21-alpine
USER spring-app:minouni
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]