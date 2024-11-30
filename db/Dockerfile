FROM postgres:latest

WORKDIR /app

COPY ../terraform/modules/rds/init.sql /docker-entrypoint-initdb.d/
