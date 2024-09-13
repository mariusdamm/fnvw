#!/bin/bash

docker compose -f ../web/docker-compose.build.yml up || exit
rm -rf ../server/src/main/resources/static/ || exit
cp -r ../web/dist/fnvw-frontend/browser/* ../server/src/main/resources/static/ || exit
docker compose -f ../server/docker-compose.build.yml up || exit
