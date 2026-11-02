<div align="center">

# Loyalty Platform — API

**API RESTful em Spring Boot que atende o painel web da Loyalty Platform (gamificação B2B).**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle-19c-F80000?logo=oracle&logoColor=white)](https://www.oracle.com/database/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Render-2496ED?logo=docker&logoColor=white)](https://render.com/)

[Links](#-links) •
[Tecnologias](#-tecnologias) •
[Arquitetura](#%EF%B8%8F-arquitetura) •
[Como executar](#%EF%B8%8F-como-executar) •
[Variáveis](#-variáveis-de-ambiente) •
[Contas de teste](#-contas-de-teste) •
[Endpoints](#-endpoints) •
[Deploy](#%EF%B8%8F-deploy) •
[Integrantes](#-integrantes)

</div>

---

## 📖 Sobre

A **Loyalty Platform** é uma plataforma B2B de gamificação: empresas clientes criam campanhas com missões, e os usuários delas acumulam pontos, níveis, ranking e sequência de dias. Esta API é a camada de back-end do painel web: faz o login do administrador da Loyalty e das empresas clientes, o cadastro e a gestão das empresas, das campanhas e das missões, e entrega o ranking e as métricas de engajamento a partir do banco Oracle.

> Projeto acadêmico desenvolvido para o **FIAP Challenge 2026**, disciplina Domain Driven Design using Java.

---

## 🔗 Links

| Item | Link |
|---|---|
| Repositório da API (este) | [Challenge-LoyaltyPlatform/Java-LoyaltyPlatform4](https://github.com/Challenge-LoyaltyPlatform/Java-LoyaltyPlatform4) |
| Repositório do front-end | [Challenge-LoyaltyPlatform/LoyaltyPlatform-Front-React](https://github.com/Challenge-LoyaltyPlatform/LoyaltyPlatform-Front-React) |
| Front-end publicado (Vercel) | [loyalty-platform-front-react.vercel.app](https://loyalty-platform-front-react.vercel.app) |
| API publicada (Render) | [loyalty-platform-api-snkp.onrender.com](https://loyalty-platform-api-snkp.onrender.com) |

---

## 🚀 Tecnologias

| Tecnologia | Uso no projeto |
|---|---|
| Java 21 | Linguagem (records, switch com `->`, streams) |
| Spring Boot 4.1.1 (Web MVC e Validation) | Endpoints REST, filtros, conversão JSON e Bean Validation |
| JDBC com pool HikariCP | Acesso ao banco com `PreparedStatement`, sem JPA |
| Oracle Database 19c (driver ojdbc17) | Banco de dados da FIAP |
| Maven | Build e dependências |
| BCrypt (spring-security-crypto) | Hash das senhas do painel |
| JWT (HS256) | Token de acesso, gerado e validado com a API de criptografia do próprio Java |
| Docker + Render | Publicação da API com HTTPS |

---

## 🏗️ Arquitetura

O fluxo de cada requisição é **Resource → BO → DAO → ConnectionFactory → Oracle**. Os pacotes ficam em `src/main/java/br/com/fiap/loyalty`:

| Pacote | Papel |
|---|---|
| `model` | Entidades do banco e regras de negócio (Empresa, Campanha, Missao, Nivel, Sequencia...) |
| `dao` | Acesso ao banco com SQL e `PreparedStatement`, uma classe por tabela |
| `bo` | Regras de negócio e transações: permissões, unicidade, status |
| `resource` | Endpoints REST (controllers) |
| `factory` | `ConnectionFactory`, ponto único que abre conexões com o Oracle |
| `filter` | `CorsFilter` e `AutenticacaoFilter`, que exige o token |
| `exception` | Exceções próprias e o `GlobalExceptionHandler` |
| `dto` | Objetos de entrada e saída em JSON |
| `security` | Geração e validação do token |
| `config` | Beans da aplicação (BCrypt e relógio no fuso de São Paulo) |
| `util` | Funções de apoio (normalização de textos e percentuais) |

```
Java-LoyaltyPlatform4/
├── database/
│   ├── 00_script_ddl_sprint3.sql     # Apaga e recria as tabelas
│   ├── 01_carga_inicial.sql          # Carga de dados da entrega de BD
│   ├── 02_consultas.sql              # Consultas da entrega de BD (não precisa rodar)
│   └── 03_alteracoes_api_java.sql    # Colunas novas de TB_EMPRESA e TB_ACESSO_PAINEL
├── src/main/java/br/com/fiap/loyalty/
├── src/main/resources/application.properties
├── .env.example                      # Modelo das variáveis de ambiente
├── Dockerfile                        # Imagem usada no deploy do Render
└── pom.xml
```

---

## ▶️ Como executar

**Ferramentas:** IntelliJ IDEA (Community ou Ultimate), JDK 21 ou mais novo, SQL Developer para o banco e Postman ou Insomnia para testar. O Maven já vem dentro do IntelliJ.

1. **Clonar e abrir.** Rode `git clone https://github.com/Challenge-LoyaltyPlatform/Java-LoyaltyPlatform4.git` e, no IntelliJ, use *File → Open* na pasta clonada. Ele reconhece o `pom.xml` e baixa as dependências; espere a barra de progresso terminar.
2. **Configurar o JDK.** Se o IntelliJ pedir um SDK, vá em *File → Project Structure → Project → SDK → Download JDK* e escolha a versão 21 (Eclipse Temurin).
3. **Criar o `.env`.** Copie o arquivo `.env.example` da raiz do projeto para `.env`, na mesma pasta, e preencha `DB_USERNAME` (seu RM, ex.: `rm572133`) e `DB_PASSWORD`. Se definir `JWT_SECRET`, ele precisa ter 32 caracteres ou mais, ou a API não sobe. O `.env` fica fora do Git.
4. **Preparar o banco** no seu schema. No SQL Developer, conecte em `oracle.fiap.com.br`, porta `1521`, SID `ORCL`. Abra cada script da pasta `database/` e rode com **F5** (*Executar Script*, não Ctrl+Enter), nesta ordem:
   1. `00_script_ddl_sprint3.sql`: **apaga e recria** as tabelas do seu schema.
   2. `01_carga_inicial.sql`: carga de dados da entrega de BD.
   3. `03_alteracoes_api_java.sql`: colunas novas e contas de login do painel.
5. **Subir a API.** Abra `src/main/java/br/com/fiap/loyalty/LoyaltyPlatformApplication.java` e clique no ▶ verde ao lado do `main`. Acesse `http://localhost:8080/` no navegador: deve aparecer `{"status":"UP", ...}`.
6. **Testar com login.** No Postman, envie `POST http://localhost:8080/auth/login` com o corpo `{"email": "admin@loyalty.com", "senha": "..."}`. Copie o `token` da resposta e mande-o nas outras chamadas no cabeçalho `Authorization: Bearer <token>`, por exemplo em `GET http://localhost:8080/empresas`.

**Problemas comuns:**

- `Port 8080 was already in use`: outro programa usa a porta. Acrescente a linha `PORT=8081` no `.env` e use `http://localhost:8081`.
- `ORA-01017` no log ou 500 "Não foi possível acessar o banco de dados": usuário ou senha errados no `.env`.
- `ORA-00942` (tabela não existe): os scripts do passo 4 não foram rodados no seu schema.

---

## 🔐 Variáveis de ambiente

Localmente, ficam no arquivo `.env` (modelo em `.env.example`). No Render, são cadastradas no painel do serviço. **O `.env` nunca vai para o Git.**

| Variável | Obrigatória | Para que serve |
|---|---|---|
| `DB_URL` | Não (padrão: Oracle da FIAP) | URL JDBC do banco, ex.: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| `DB_USERNAME` | Sim | Usuário do banco (RM) |
| `DB_PASSWORD` | Sim | Senha do banco |
| `JWT_SECRET` | Sim no deploy | Chave que assina o token; texto aleatório com 32 caracteres ou mais |
| `CORS_ORIGENS` | Não | Origens do front liberadas, separadas por vírgula; `*` vale para um subdomínio (ex.: `https://*.vercel.app`) |
| `PORT` | Não (padrão: 8080) | Porta HTTP; o Render define sozinho |
| `JWT_EXPIRACAO_HORAS` | Não (padrão: 8) | Validade do token |
| `DB_POOL_SIZE` | Não (padrão: 3) | Conexões no pool; o Oracle da FIAP limita as sessões por aluno |

---

## 👤 Contas de teste

Criadas pelo script `03_alteracoes_api_java.sql`. A senha das quatro está no comentário do próprio script, logo acima dos `INSERT`.

| E-mail | Perfil | Empresa | Resultado do login |
|---|---|---|---|
| `admin@loyalty.com` | ADMIN | — | 200, acessa tudo |
| `empresa@soulup.com.br` | EMPRESA | SoulUp Educação (1) | 200, só dados da SoulUp |
| `painel@finplus.com.br` | EMPRESA | FinPlus Investimentos (2) | 200, só dados da FinPlus |
| `painel@fitzone.com.br` | EMPRESA | FitZone Academias (5) | 403, a empresa está inativa |

---

## 📡 Endpoints

Fora `GET /` e `POST /auth/login`, todas as rotas exigem o cabeçalho `Authorization: Bearer <token>`. "EMPRESA dona" significa que o perfil EMPRESA só acessa os dados da própria empresa.

| Verbo | URI | O que faz | Quem pode | Sucesso | Erros |
|---|---|---|---|---|---|
| GET | `/` | Confere se a API está no ar | Pública | 200 | — |
| POST | `/auth/login` | Login: devolve o token e os dados do usuário | Pública | 200 | 400, 401, 403 |
| GET | `/empresas` | Lista todas as empresas | ADMIN | 200 | 401, 403 |
| GET | `/empresas/{id}` | Detalhe de uma empresa | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| POST | `/empresas` | Cadastra empresa (nasce ATIVA) | ADMIN | 201 | 400, 401, 403, 409 |
| PUT | `/empresas/{id}` | Atualiza dados cadastrais e identidade visual | ADMIN | 200 | 400, 401, 403, 404, 409 |
| PATCH | `/empresas/{id}/status` | Inativa (encerra as campanhas ativas) ou reativa | ADMIN | 200 | 400, 401, 403, 404, 409 |
| GET | `/empresas/{idEmpresa}/campanhas` | Lista as campanhas com os totais de missões | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| POST | `/empresas/{idEmpresa}/campanhas` | Cria campanha (nasce INATIVA) | ADMIN ou EMPRESA dona | 201 | 400, 401, 403, 404, 409 |
| GET | `/campanhas/{id}` | Detalhe da campanha com as missões | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| PUT | `/campanhas/{id}` | Edita nome, descrição e datas | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404, 409 |
| PATCH | `/campanhas/{id}/status` | Muda para ATIVA, INATIVA ou ENCERRADA | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404, 409 |
| DELETE | `/campanhas/{id}` | Exclui rascunho inativo sem participantes | ADMIN ou EMPRESA dona | 204 | 400, 401, 403, 404, 409 |
| GET | `/campanhas/{idCampanha}/missoes` | Lista as missões da campanha | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| POST | `/campanhas/{idCampanha}/missoes` | Cria missão (nasce ATIVA) | ADMIN ou EMPRESA dona | 201 | 400, 401, 403, 404, 409 |
| GET | `/missoes/{id}` | Detalhe da missão | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| PUT | `/missoes/{id}` | Edita nome, descrição, meta e pontos | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404, 409 |
| PATCH | `/missoes/{id}/status` | Muda para ATIVA ou INATIVA | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404, 409 |
| DELETE | `/missoes/{id}` | Exclusão lógica (status EXCLUIDA) | ADMIN ou EMPRESA dona | 204 | 400, 401, 403, 404, 409 |
| GET | `/empresas/{idEmpresa}/ranking` | Ranking de usuários da empresa | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |
| GET | `/empresas/{idEmpresa}/metricas` | Engajamento geral e por campanha | ADMIN ou EMPRESA dona | 200 | 400, 401, 403, 404 |

Todos os erros vêm no formato `{ status, mensagem, campos }`, e `campos` só aparece quando há erro em um campo específico:

```json
{
  "status": 409,
  "mensagem": "Já existe empresa com esses dados.",
  "campos": { "cnpj": "Este CNPJ já está cadastrado (FinPlus Investimentos). Confira o número ou reative a empresa existente." }
}
```

| Código | Quando acontece |
|---|---|
| 400 | Dados inválidos, com a mensagem de cada campo em `campos`, ou id não numérico na URL |
| 401 | E-mail ou senha incorretos, ou token ausente, inválido ou expirado |
| 403 | Perfil sem permissão, dados de outra empresa, ou login de empresa inativa |
| 404 | Registro não existe (inclusive missão excluída) ou rota inexistente |
| 405 | Verbo não suportado na rota, como `DELETE /empresas/{id}` (empresa é inativada, não excluída) |
| 409 | Regra de negócio: CNPJ ou e-mail duplicado, status já aplicado, campanha encerrada, ativar campanha sem missão ativa |
| 415 | Corpo enviado sem `Content-Type: application/json` |
| 500 | Erro inesperado ou banco fora do ar; pode ocorrer em qualquer rota |

---

## ☁️ Deploy

A API roda no **Render**, a partir do `Dockerfile` da raiz (o Render só roda Java por Docker e já entrega HTTPS). As variáveis da seção acima são cadastradas no painel do serviço, e o *Health Check Path* é `/`.

> O plano gratuito do Render coloca a API para dormir depois de 15 minutos sem uso. A primeira chamada depois disso leva cerca de 1 minuto; abra a URL da API e espere o `{"status":"UP", ...}` antes de usar o front.

---

## 👥 Integrantes

Equipe **HalfTech**, turma **1TDSPW**.

| Nome | RM | Turma |
|---|---|---|
| Andrei Oliveira de Sousa | 573373 | 1TDSPW |
| Felipe Coelho da Fonseca | 572133 | 1TDSPW |
| Henrique Soares Pereira | 569898 | 1TDSPW |
| Luiz Felipe Lourenço da Silva | 572576 | 1TDSPW |

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins de estudo no **FIAP Challenge 2026**. Uso restrito a fins educacionais.
