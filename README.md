# Tech Challenge Application

## Descrição

Este  é um microserviço para gerenciar o cadastro e buscar de produto e usuários.
## Funcionalidades

- **Gerenciamento de Produtos**: CRUD de produtos, categorias, listagem por disponibilidade e categoria.
- **Usuários**: Gerenciamento de clientes (incluindo anônimos) e atendentes.
- **Documentação da API**: Swagger UI para exploração da API.

## Tecnologias Utilizadas

- **Java**: 17
- **Spring Boot**: 3.4.5
- **Banco de Dados**: MySQL (produção), H2 (testes)
- **ORM**: JPA/Hibernate
- **Documentação**: SpringDoc OpenAPI (Swagger)
- **Containerização**: Docker
- **Orquestração**: Kubernetes

## Arquitetura

O projeto segue os princípios de Clean Architecture, dividido em camadas:

- **_webApi**: Controladores REST, DTOs e mapeadores.
- **core**: Lógica de negócio, entidades de domínio, casos de uso, controladores de aplicação e gateways.
- **interfaces**: Interfaces para fontes de dados.
- **persistence**: Implementações de repositórios JPA.

## Pré-requisitos

- Java 17
- Maven 3.6+
- MySQL (ou Docker para containerização)
- Conta no Mercado Pago para tokens de acesso
- Conta Gmail para notificações por e-mail (ou SMTP configurado)

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/usuario/tech-challenge-application.git
   cd tech-challenge-application
   ```

2. Instale as dependências:
   ```bash
   mvn clean install
   ```

## Configuração

Configure as variáveis de ambiente no arquivo `application.properties` ou via variáveis de sistema:

- `SPRING_DATASOURCE_URL`: URL do banco MySQL
- `SPRING_DATASOURCE_USERNAME`: Usuário do banco
- `SPRING_DATASOURCE_PASSWORD`: Senha do banco

## Execução

### Local

1. Certifique-se de que o MySQL está rodando.
2. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```
3. Acesse a aplicação em `http://localhost:8080`

### Com Docker

1. Construa a imagem:
   ```bash
   docker build -t tech-challenge .
   ```
2. Execute o container:
   ```bash
   docker run -p 8080:8080 tech-challenge
   ```

### Com Kubernetes

Aplique o deployment:
```bash
kubectl apply -f k8s/deployment.yml
```

## Documentação da API

Acesse o Swagger UI em `http://localhost:8080/swagger-ui.html` para explorar e testar os endpoints.

Principais endpoints:

- **Produtos**: `/api/product/*`
- **Clientes**: `/api/user/customer/*`
- **Atendentes**: `/api/user/attendant/*`

## Testes

Execute os testes com:
```bash
mvn test
```

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto é licenciado sob a MIT License.
