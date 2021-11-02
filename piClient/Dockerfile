FROM node:lts-alpine

# set working directory
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# add app
ADD . ./

# install app dependencies
RUN npm install
RUN npm install -g @angular/cli
RUN npm install -g serve

# build the app
RUN ng build --prod

EXPOSE 8080

# use serve to host the static file
CMD serve -s ./dist/piClient -l 8080
