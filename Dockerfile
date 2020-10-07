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
RUN deno cache backend/src/app.ts
RUN deno install --unstable -qAf https://deno.land/x/denon@2.4.0/denon.ts

# Swap to the backend for the runtime envrionment
WORKDIR /app/backend