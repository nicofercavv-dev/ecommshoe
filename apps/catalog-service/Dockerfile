# Estágio 1: Compilação (Build)
FROM gradle:8-jdk21 AS build
WORKDIR /home/gradle/src

# 1. Copia os arquivos de configuração globais do monorepo
COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/

# [OPCIONAL] Se você tiver outros subprojetos dos quais o catalog-service depende (ex: libs compartilhadas),
# você precisará copiar o build.gradle deles aqui para o Gradle mapear a árvore de dependências.
# COPY --chown=gradle:gradle libs/core/build.gradle /home/gradle/src/libs/core/

# 2. Copia a estrutura do subprojeto catalog-service
COPY --chown=gradle:gradle apps/catalog-service/build.gradle /home/gradle/src/apps/catalog-service/
COPY --chown=gradle:gradle apps/catalog-service/src /home/gradle/src/apps/catalog-service/src

# Executa o build focando apenas no subprojeto.
# Ajuste o nome da task (:apps:catalog-service ou :catalog-service) conforme definido no seu settings.gradle
RUN gradle :apps:catalog-service:bootJar --no-daemon -x test

# Estágio 2: Execução (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria um usuário do sistema para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia especificamente o bootJar gerado (evitando o -plain.jar se houver)
# Nota: Se a task for :apps:catalog-service, o caminho do build costuma ser o abaixo:
COPY --from=build /home/gradle/src/apps/catalog-service/build/libs/*-SNAPSHOT.jar app.jar
# Se a versão não for SNAPSHOT, você pode usar: app.jar (ou garantir o nome exato no build.gradle)

# Porta do microsserviço
EXPOSE 8081

# Execução com flags de otimização para container (o Java 21 já gerencia bem isso)
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]