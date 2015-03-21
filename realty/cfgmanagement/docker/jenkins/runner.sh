#!/usr/bin/env bash

#sudo docker build -rm -t rent4me/jenkins .
#sudo docker run -p 8080:8080 -i rent4me/jenkins
docker run --name rent4me-jenkins -p 8080:8080 -v jenkins:/var/jenkins_home jenkins





