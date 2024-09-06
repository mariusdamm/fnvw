docker compose -f ../web/docker-compose.build.yml up --build || exit
cp -r ../web/dist/fnvw-frontend/browser/* ../server/src/main/resources/static/ || exit
docker compose -f ../server/docker-compose.build.yml up --build || exit
