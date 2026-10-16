# ----- Stage 1: Build -----
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Αντιγραφή του ριζικού pom.xml και των υπο-έργων
COPY pom.xml .
COPY citizen-entities ./citizen-entities
COPY citizen-rest ./citizen-rest
COPY citizen-client ./citizen-client

# Μεταγλώττιση και δημιουργία του executable jar 
RUN mvn clean package -DskipTests

# ----- Stage 2: Runtime -----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Δημιουργία μη ριζικού χρήστη (Non-root user) για ασφάλεια
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Αντιγραφή του παράγωγου .jar από το stage 1 (από το υπο-έργο citizen-rest)
COPY --from=builder /app/citizen-rest/target/*.jar app.jar

# Αλλαγή δικαιωμάτων στον μη ριζικό χρήστη
RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]