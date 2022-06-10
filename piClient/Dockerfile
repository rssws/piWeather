# BUILD
FROM node:lts-alpine as build

# set working directory
WORKDIR /build

ARG PI_API
ENV PI_API $PI_API
ARG VERSION

ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY ./package.json ./yarn.lock ./
RUN sed -i -E 's/(\ *"version": )"dev",/\1"'"$VERSION"'",/g' package.json
RUN sed -i -E 's/(\ *"version": )"dev",/\1"'"$VERSION"'",/g' package-lock.json
RUN yarn install --frozen-lockfile
RUN yarn add -g @angular/cli

COPY ./script ./script
COPY ./tsconfig.json ./tsconfig.app.json ./.env.production ./angular.json ./
COPY ./src ./src

# build the app
RUN yarn build:prod

# PRODUCTION
FROM node:lts-slim
WORKDIR /piClient
COPY --from=build /build/dist .

RUN yarn add -g serve

# use serve to host the static file
CMD serve -l 8080 ./piClient
