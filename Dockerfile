# Etapa 1: compila o projeto com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Baixa as dependências antes de copiar o código: se só o código mudar, o Docker reaproveita esta camada
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# Etapa 2: imagem final só com o Java e o .jar (o finalName do pom.xml gera loyalty-platform-api.jar)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/loyalty-platform-api.jar app.jar
# Roda sem privilégio de root
RUN useradd --system --no-create-home loyalty
USER loyalty
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Duser.timezone=America/Sao_Paulo"
# O Render informa a porta pela variável PORT; o application.properties usa 8080 se ela não existir
EXPOSE 8080
# exec: o Java vira o processo principal e recebe o sinal de desligamento do Render
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
