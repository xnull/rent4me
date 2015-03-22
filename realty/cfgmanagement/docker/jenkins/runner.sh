#!/usr/bin/env bash

mkdir -p ${PWD}/jenkins-data
sudo chmod 777 ${PWD}/jenkins-data

docker run -p 8080:8080 -v ${PWD}/jenkins-data:/var/jenkins_home jenkins
