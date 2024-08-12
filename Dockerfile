FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM openjdk:21

COPY --from=build /app/target/fastfood-api-1.0.0.jar /app/app.jar

WORKDIR /app

ARG PORT=8080
ARG CURRENT_ENV=dev-local
ARG DATABASE_URL_ARG=jdbc:postgresql://localhost:5432/postgres
ARG DATABASE_USERNAME=postgres
ARG DATABASE_PASSWORD=postgrespw

ENV FASTFOOD_API_PORT=${PORT}
ENV FASTFOOD_API_ENVIRONMENT=${CURRENT_ENV}

EXPOSE ${PORT}

ENV FASTFOOD_DATABASE_URL=${DATABASE_URL_ARG}
ENV FASTFOOD_DATABASE_USER=${DATABASE_USERNAME}
ENV FASTFOOD_DATABASE_PASSWORD=${DATABASE_PASSWORD}

RUN echo "### This is a basic dockerfile to Tech Challenge ###"
RUN echo "The application will start in port: ${FASTFOOD_API_PORT};"

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${FASTFOOD_API_ENVIRONMENT}", "app.jar"]
