# Stage №1 - Build fat .jar
FROM maven:3.9.9-amazoncorretto-21-debian AS build
WORKDIR /app
COPY --chown=spring-app:minouni . /app
RUN mvn clean package -DskipTests

# Stage №2 - Extract layers
FROM maven:3.9.9-amazoncorretto-21-debian AS builder
WORKDIR /app
COPY --from=build /app/build/libs/mentality-*.jar /app/mentality.jar
RUN java -Djarmode=layertools -jar mentality.jar extract

# Stage №3 - Copy extracted layers
FROM maven:3.9.9-amazoncorretto-21-debian
USER spring-app:minouni
WORKDIR /app
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]