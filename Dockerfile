# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1) Cache deps
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# 2) Build
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy the fat jar produced by spring-boot-maven-plugin
COPY --from=build /app/target/registration-api-1.0.0.jar /app/app.jar

# Render/Heroku-style: use $PORT if provided, default to 8000
EXPOSE 8000
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8000} -jar /app/app.jar"]
