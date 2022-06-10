# BUILD
FROM node:lts-alpine as build

# set working directory
WORKDIR /build

ARG PI_API
ENV PI_API $PI_API

ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY ./package.json ./package-lock.json ./
RUN npm ci
RUN npm install -g @angular/cli

COPY ./script ./script
COPY ./tsconfig.json ./tsconfig.app.json ./.env.production ./angular.json ./
COPY ./src ./src

# build the app
RUN npm run build:prod

# PRODUCTION
FROM node:lts-slim
WORKDIR /piClient
COPY --from=build /build/dist .

RUN npm install -g serve

# use serve to host the static file
CMD serve -l 8080 ./piClient
