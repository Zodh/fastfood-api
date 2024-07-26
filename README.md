# Fast food APIs - Tech Challenge - 8SOAT FIAP

Este projeto é o trabalho feito pelo time 15 para solucionar o desafio técnico do tech challenge. O projeto se consiste em um conjunto de APIs desenvolvidas usando as técnicas ensinadas durante o curso.

## Buildando e rodando o projeto

Atualmente existem as seguintes maneiras de se executar o projeto (do mais simples ao mais difícil):

### Docker compose

1. execute o comando `mvn clean package`;
2. na pasta do projeto, execute o comando: `docker-compose up`.

OBS: precisa parametrizar a senha do envio de email na variável: FASTFOOD_MAIL_PASSWORD.


1. execute o comando `mvn clean package`;
2. execute o comando: `docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=postgrespw postgres:latest`;
3. execute a aplicação na sua maquina.

OU

1. execute o comando `mvn clean package`;
2. execute o postgres na sua máquina configurando usuario postgres e a senha para postgrespw;
3. execute a aplicação na sua maquina.

OBS: lembre-se de usar o `dev-local.env` como arquivo de variáveis de ambiente.

### Dockerizando a aplicação e o banco

1. execute o comando: `docker network create --driver bridge minha-rede`;
2. execute o comando: `docker run -d --name fastfood-db --net=minha-rede -p 5432:5432 -e POSTGRES_PASSWORD=postgrespw postgres:latest`;
3. na pasta do projeto, execute o comando: `mvn clean package`;
4. na pasta do projeto, execute o comando `docker build --build-arg="DATABASE_URL_ARG=jdbc:postgresql://fastfood-db:5432/postgres" -t zodh/fastfood-api:1.0.0 .`;
5. execute o comando `docker run -d -p 8080:8080 --net=minha-rede zodh/fastfood-api:1.0.0`.


### Keycloak

Para subir o keycloak, execute o comando: `docker run -p 9191:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.1 start-dev`.