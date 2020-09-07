# DESAFIO SAMBA TECH

[![Build Status](https://travis-ci.org/WenderGalan/desafio-sambatech.svg?branch=master)](https://travis-ci.org/WenderGalan/desafio-sambatech)
[![codecov](https://codecov.io/gh/WenderGalan/desafio-sambatech/branch/master/graph/badge.svg)](https://codecov.io/gh/WenderGalan/desafio-sambatech)

- Informações do projeto: 
    - O mesmo está integrado ao [Travis CI (Integração continua)](https://travis-ci.org/github/WenderGalan/desafio-sambatech), [HEROKU (Deploy automático)](https://desafio-sambatech.herokuapp.com/swagger-ui.html), e [CodeCov (Cobertura de código)](https://codecov.io/gh/WenderGalan/desafio-sambatech). Tudo de forma automática.
    - Para fazer o teste da aplicação basta [clicar aqui](https://desafio-sambatech.herokuapp.com/swagger-ui.html) que será redirecionado a API do heroku é possível que demore alguns segundos para iniciar.
    - O [Open API](https://github.com/WenderGalan/desafio-sambatech/blob/master/open-api.json) está disponível na raiz do projeto.
    - A [coleção do postman](https://github.com/WenderGalan/desafio-sambatech/blob/master/postman_collection.json) está disponível na raiz do projeto. Apenas importe para o Postman.
    - A ACCESS_KEY_ID e SECRET_ACCESS_KEY foi enviada junto ao link do repositório aos recrutadores. Não pode ser colocada no código devido a segurança da AWS.
    - O limite de tamanho de arquivos que podem ser enviados foi limitado a 100 MB na aplicação.
    - O deploy deixei de forma automática no heroku, caso os testes falhem não irá fazer o deploy, devido minha conta na AWS já ter expirado o tempo gratuito não consegui fazer o deploy da aplicação lá.
    
- Observação:
    - Fiquei com algumas dúvidas referente ao que a API deveria receber como parâmetro e body nos controllers e fiz da forma que imaginei que seria tratado a informação. Devido ser final de semana acabei não mandando e-mail para tirar as dúvidas.

#### Como rodar o projeto em sua máquina?
   Assumindo que você já tenha o ambiente de desenvolvimento com o JAVA 11, Postgres instalados e configurados você pode entrar no application.properties do projeto e alterar as propriedades para as informações de seu ambiente, e rodar o comando abaixo:
```
   mvn spring-boot:run
```

   Para rodar os testes na máquina execute ```mvn test``` caso esteja utilizando o docker ele irá fazer os testes antes da aplicação iniciar, caso algum falhe ele não irá iniciar o back-end.

#### Utilizando o docker

Assumindo que já tenha o Docker instalado na máquina, execute os procedimentos abaixo.

- Insira a ACCESS_KEY_ID e a SECRET_ACCESS_KEY dentro do common.env para que seja autorizado o acesso ao BUCKET da AWS.
- Abra o console na pasta raiz da aplicação e execute o comando abaixo (devido a biblioteca do XUGGLE a primeira build do docker está demorada):
  
```
  docker-compose up -d --build
```

No seu browser acesse as urls:
- Back-end: http://localhost:8080/swagger-ui.html
    - USER: admin
    - PASSWORD: admin
- PG ADMIN 4: http://localhost:16543
    - USER: postgres@localhost
    - PASSWORD: admin
- Banco de dados: http://localhost:5432
    - USER: postgres
    - PASSWORD: admin

#### Dependências:
  - JPA Data: Gerenciamento das entidades da aplicação
  - Spring Security: Segurança da api (Basic Auth)
  - Spring Email: Usado para o envio de e-mails dentro da aplicação
  - Spring Web
  - Spring DevTools
  - Postgres: Banco de dados
  - Lombok: Biblioteca para evitar escrita de código desnecessário
  - Swagger: Documentar a API
  - H2: Banco de dados em memória para testes.
  - XUGGLE: Biblioteca para trabalhar com videos.
  - Hibernate Validator: Validação das entradas
  - Amazon AWS: Usado para a conexão com o bucket da amazon.
 
#### Integração contínua: [Travis CI](https://travis-ci.org/)
   - O Travis CI é um serviço web de Integração Contínua na nuvem integrado com o GitHub.

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://desafio-sambatech.herokuapp.com/swagger-ui.html)