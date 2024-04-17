# First build stage
FROM amazoncorretto:17-alpine-jdk as builder
WORKDIR /build
COPY . .
RUN ./mvnw clean package

# Running container
FROM amazoncorretto:17-alpine-jdk
COPY --from=builder /build/target/seat-robotic-mower-control-0.0.1-SNAPSHOT.jar /seat-robotic-mower-control-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/seat-robotic-mower-control-0.0.1-SNAPSHOT.jar"]
