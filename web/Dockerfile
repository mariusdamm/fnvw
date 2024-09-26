FROM node:slim
WORKDIR /app
COPY package.json .
COPY package-lock.json .
COPY angular.json .
RUN npm install

CMD ["npm", "run", "build"]
