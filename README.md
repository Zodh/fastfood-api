# Fast food APIs - Tech Challenge - 8SOAT FIAP

Nome da equipe: Team 15

### Contato dos participantes do projeto: 

- Discord: Felipe Carvalho - RM357341 / @ifelipedev
- WhatsApp: (19) 99204-5958 (Felipe Carvalho)


- Discord: Guilherme Meireles - RM357216 / @guilherme100

### Descrição

Este projeto é o trabalho feito pelo time 15 para solucionar o desafio técnico do tech challenge. O projeto se consiste em um conjunto de APIs desenvolvidas usando as técnicas ensinadas durante o curso, essas APIs servem para atender as necessidades de uma lanchonete de bairro.

### Rodando projeto pelo K8S
Lembre-se de ter o kubectl instalado em sua máquina, e caso queira criar clusters Kubernetes locais usando o Docker,
você pode utilizar o kind.

Na pasta k8s/develop, execute os comandos em sequência:

```
kubectl apply -f fastfood-secret.yaml

kubectl apply -f fastfood-db-statefulset.yaml

kubectl apply -f fastfood-db-service.yaml

kubectl apply -f fastfood-api-deployment.yaml

kubectl apply -f fastfood-api-service.yaml

```

Para conseguir realizar as requisições em sua máquina, digite o comando: kubectl port-forward <NOME_DO_POD> 8080:8080.

## Trello (atividades de desenvolvimento do projeto):
[Trello - Tech Challenge 8SOAT FIAP / Team 15](https://trello.com/b/RRTCdSx4/8soat-time-15)

## Miro (desenho da solução do projeto)
[Modelagem - MIRO](https://miro.com/app/board/uXjVK5Fs-r0=/)

## Rodando o projeto

Na pasta do projeto, execute o comando: `docker-compose up`.

## Verificando APIs do projeto

Um arquivo com todas as requisições (Postman) foi adicionado na raiz do projeto. Contudo, para seguir conforme o solicitado, disponibilizamos as APIs do projeto no swagger abaixo:

Acessar http://localhost:8080/swagger-ui/index.html#/ quando a aplicação estiver rodando.
