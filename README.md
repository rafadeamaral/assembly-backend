# Assembly back-end

Durante uma assembleia, após ser apresentada uma pauta a mesma é aberta para votação. Com isso o sistema permite
o cadastro do evento da assembleia e após seu cadastro é permitido cadastrar as pautas. Com a pauta cadastrada é
permitido abrir a mesma para votação e ao seu término é contabilizado e apresentado os votos. Para votar o
associado deve estar cadastrado no sistema.

## Tecnologias utilizadas
- Java 11
- Spring
- Lombok
- Mysql
- Model Mapper
- Swagger
- RabbitMQ
- JUnit
- Maven
- Docker

## Executar aplicação

Deve estar com o Docker rodando na máquina e ter instalado e configurado o Apache Maven.

No terminal, executar os seguintes comandos (deve estar na pasta raiz do projeto):
- mvn clean install
- docker-compose up

## Documentação da API

Para a documentação da API foi utilizado o Swagger.

Após rodar a aplicação é possível acessá-la pela URL:
- http://localhost:8080/swagger-ui.html

Foi adicionado ao projeto a collection do Postman: 
- [Postman Collection](Assembly.postman_collection.json)

## Aplicações

### assembly-backend

Aplicação back-end para gerenciamento das pautas.

### assembly-producer

Aplicação responsável para produzir as mensagerias.

### assembly-consumer

Aplicação responsável para consumir as mensagerias.

## Versionamento da API

Em ambiente corporativo o versionando é possível pelo GitLab ou Bitbucket. Onde o projeto versionado a um grupo, 
é possível fazer um fork com a branch de trabalho. Ao término do desenvolvimento deve ser feito um pull request
da origin para a upstream, onde o mesmo deve ser feito a revisão do código.
