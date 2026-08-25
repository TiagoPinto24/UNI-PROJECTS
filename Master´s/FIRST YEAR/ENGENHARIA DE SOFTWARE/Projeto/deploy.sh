#!/bin/bash

set -e

echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

docker compose -f docker-compose.prod.yml pull

docker compose -f docker-compose.prod.yml down
docker compose -f docker-compose.prod.yml up -d

docker image prune -f
