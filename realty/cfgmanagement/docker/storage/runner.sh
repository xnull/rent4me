#!/usr/bin/env bash

mkdir -p ${PWD}/drive
sudo chmod 777 ${PWD}/drive

#sudo docker rmi $(sudo docker images -qf "dangling=true")
sudo docker build -t rent4me/storage:v1 .

sudo docker --log-level=debug run -d ${PWD}/drive:/var/google-drive rent4me/storage:v1
