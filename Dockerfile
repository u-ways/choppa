### Node to build the frontend
FROM node:12 AS build-ui
COPY . /app
WORKDIR /app/frontend
RUN npm ci
RUN npm run build

### Deno for the backend and runtime
FROM hayd/deno:ubuntu-1.4.2 AS runtime
COPY --from=build-ui /app /app
WORKDIR /app
RUN deno cache --unstable backend/src/app.ts
RUN deno install -qAf --unstable https://deno.land/x/denon@2.4.0/denon.ts