# builder stage: build the jar with Maven (uses Maven image with JDK)
FROM maven:3.9.6-eclipse-temurin-17 as builder
WORKDIR /app

# copy wrapper and pom first for caching (if you use wrapper)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# make wrapper executable & normalize line endings if present
RUN if [ -f ./mvnw ]; then chmod +x ./mvnw && sed -i 's/\r$//' ./mvnw; fi

# download dependencies (use wrapper if present otherwise mvn)
RUN if [ -f ./mvnw ]; then ./mvnw -B -DskipTests dependency:go-offline; else mvn -B -DskipTests dependency:go-offline; fi

# copy source and build
COPY src ./src
RUN if [ -f ./mvnw ]; then ./mvnw -B -DskipTests package; else mvn -B -DskipTests package; fi

# runtime stage: slim JRE image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# recommended env hooks for Render
ENV JAVA_OPTS=""
EXPOSE 8080

# allow Render's $PORT to set server port and let user add JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
