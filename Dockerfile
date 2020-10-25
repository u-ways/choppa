FROM node:12 AS build-ui
COPY . /app
WORKDIR /app/src/frontend
RUN npm ci
RUN npm run build

FROM gradle:6.7-jdk11 AS runtime
COPY --from=build-ui /app /app
WORKDIR /app
RUN ./gradlew bootJar