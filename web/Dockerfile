FROM node:22.6.0-bookworm
WORKDIR /app
COPY package.json .
COPY angular.json .
RUN npm install

CMD ["npm", "run", "build"]
