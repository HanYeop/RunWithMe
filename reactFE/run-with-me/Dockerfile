FROM node:16.14.0

WORKDIR /usr/src/app

COPY package.json ./
COPY ./ ./
RUN npm install
RUN npm run build
RUN yarn global add serve

COPY ./ ./

CMD [ "serve", "-s", "build" ]
