# API Loja Produtos

# Tecnologias

## Backend
- Java (Versão 17)
- Maven
- Lombok
- Spring Boot
- JPA/Hibernate
- Spring Security
- MockMvc e JUnit
- Bancos de dados PostgresSQL
- Flyway
- Banco de dados H2 (Testes integrado)
- Swagger (Documentação)
- Jacoco
- Docker
- RabbitMQ

## Sobre o projeto

Esta aplicação consiste realizar um pedido, finalizar ou até mesmo cancelar para um usuário. Este usuário consegue consultar seus pedidos realizados e os itens. O usuário pode ter três tipos de perfis (ADMIN, FUNCIONARIO, CLIENTE).

![Cadastro de um pedido](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/cadastro_pedido.png)

Após cadastrar o pedido, o usuário vai receber um e-mail sobre este pedido.

![E-mail cadastro de pedido](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/pedido_aberto.png)

Finalizar o pedido também vai ser possível receber um e-mail.

![E-mail finalizar pedido](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/pedido_finalizado.png)

Cancelar o pedido também vai ser possível receber um e-mail.

![E-mail cancelar pedido](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/pedido_cancelado.png)


## Diagrama arquitetural
![Diagrama arquitetural](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/diagrama_arquitetura.png)

# Como executar o projeto

## Backend 

Pré requisito: Java 17 e Docker

```
# clonar o projeto loja produto api
git clone https://github.com/zGabrielZ/loja-produtos.git

# clonar o projeto que está com o script do docker yaml do banco de dados postgressql e também o rabbitmq, isso é o ambiente dev
git clone https://github.com/zGabrielZ/configs.git

# entrar na pasta do projeto que consiste o script o docker yaml do postgres
 cd '.\Config API Produto\postgres-dev\'

# executar o script docker yaml
docker-compose up -d

# entrar na pasta do projeto que consiste o script o docker yaml do rabbitmq
 cd '.\Config API Produto\rabbitmq-dev\'

# executar o script docker yaml
docker-compose up -d

# apos isso, entrar na pasta backend do projeto service registry, api produtos, api notificacao, gateway(nesta sequencia) e subir as quatro aplicação com o seguinte comando 
./mvnw spring-boot:run

# para conseguir enviar o e-mail com o host do gmail, tem que colocar os parâmetros (SMTP, SMTP_PORTA, EMAIL_REMETENTE, SENHA_REMETENTE) na sua variavel de ambiente da sua máquina
# é necessário gerar a SENHA DE APP da sua conta gmail
```

Após executar o projeto, é possível ver o service registry e a documentação da api produtos e notificação.

Usuário do service registry: serviceregistry

Senha do service registry: produtos123sr

Url: http://localhost:8761/

![Service Registry](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/service_registry.png)

Url documentação API Produto: http://localhost:8081/api-produtos/swagger-ui/index.html#/

![Documentação API Produto](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/doc_api_produto.png)

Url documentação API Notificação: http://localhost:8082/api-notificacao/swagger-ui/index.html#/

![Documentação API Notificação](https://github.com/zGabrielZ/assets/blob/main/API%20Produto/doc_api_notificacao.png)

# Autor

Gabriel Ferreira

https://www.linkedin.com/in/gabriel-ferreira-4b817717b/



