# ---------- build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN if [ -f ./mvnw ]; then ./mvnw -q -DskipTests package; else mvn -q -DskipTests package; fi
RUN cp $(ls target/*-SNAPSHOT.jar 2>/dev/null || ls target/*.jar) /app/app.jar

# ---------- run stage ----------
FROM gcr.io/distroless/java21
WORKDIR /app
COPY --from=build /app/app.jar /app/app.jar
EXPOSE 8080
USER nonroot:nonroot
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70.0"
ENTRYPOINT ["java","-jar","/app/app.jar"]
