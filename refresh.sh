#!/usr/bin/env bash

while [ $# -gt 0 ]; do
  case $1 in
    git) PULL_GIT=yes;;
  esac
  shift
done


if [ -n "$PULL_GIT" ]; then
  git pull
fi

docker-compose -f docker-compose.yml down
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose -f docker-compose.yml up --build -d