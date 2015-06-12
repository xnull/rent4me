#!/usr/bin/env bash

echo before_kill
docker kill you
docker stop you
docker rm you
echo ater_kill

docker build -t rent4me/db:v1 .
docker --log-level=debug run --name=you -d -p 5432:5432 rent4me/db:v1

