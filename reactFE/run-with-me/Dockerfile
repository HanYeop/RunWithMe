FROM node:16.14.0

WORKDIR /usr/src/app

COPY package.json ./
RUN npm run build
RUN yarn global add serve
RUN npm install

COPY ./ ./

CMD [ "serve", "-s", "build" ]
