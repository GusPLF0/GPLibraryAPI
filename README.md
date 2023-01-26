# API de Gerenciamento de Livros

Esta API permite que os usuários gerem uma biblioteca de livros com operações CRUD. Ele também inclui recursos como versionamento, migrações de banco de dados com Flyway, content negotiation, HATEOS, documentação Swagger e autenticação.

## Confira o repositório da aplicação no Docker Hub
[![Docker Hub Repo](https://img.shields.io/docker/pulls/gusplf/rest-with-spring-boot-gus.svg)](https://hub.docker.com/repository/docker/gusplf/rest-with-spring-boot-gus)

## Endpoints

- `GET /api/book/v1`: Recupera uma lista de todos os livros na biblioteca
- `GET /api/book/v1/:id`: Recupera um livro específico pelo seu ID
- `POST /api/book`: Adiciona um novo livro à biblioteca
- `PUT /api/book/:id`: Atualiza um livro existente
- `DELETE /api/book/:id`: Exclui um livro da bibliotec

## Versionamento

A API usa versionamento na URL para garantir compatibilidade com versões anteriores. A versão atual é v1.

## Flyway

A API usa o Flyway para gerenciar as migrações do banco de dados. As migrações podem ser encontradas na pasta `db/migrations`.

## Content Negotiation

A API suporta content negotiation, permitindo que os clientes solicitem um formato específico para a resposta (por exemplo, JSON, XML).

## HATEOS

A API usa HATEOAS (Hypermedia como Motor do Estado da Aplicação) para fornecer links para recursos relacionados na API.

## Swagger

A API inclui a documentação Swagger, que pode ser acessada no ponto de extremidade `/swagger`.

## Autenticação

A API usa tokens JWT (JSON Web Token) para autenticação.

## Em breve: implantação da API na Amazon AWS
