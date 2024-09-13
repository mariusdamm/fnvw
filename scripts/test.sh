#!/bin/bash

docker compose -f ../server/docker-compose.test.yml up -d
docker logs -f fnvw-app-test
docker wait fnvw-app-test
docker compose -f ../server/docker-compose.test.yml down
