#!/usr/bin/env bash

mkdir -p ${PWD}/drive
sudo chmod 777 ${PWD}/drive

docker build -t rent4me/storage:v1 .

docker run -t -i -v ${PWD}/drive:/var/google-drive rent4me/storage:v1
