FROM openjdk:21

WORKDIR /apps

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

COPY /target/*.jar app.jar

RUN echo "### This is a basic dockerfile to Tech Challenge ###"
RUN echo "The application will start in port: ${FASTFOOD_API_PORT};"

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${FASTFOOD_API_ENVIRONMENT}","app.jar"]
