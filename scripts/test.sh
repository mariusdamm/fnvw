#!/bin/bash
sudo docker compose -f ../server/docker-compose.test.yml up --build -d
sudo docker logs -f fnvw-app-test
sudo docker wait fnvw-app-test
sudo docker compose -f ../server/docker-compose.test.yml down
