#!/usr/bin/env bash

docker run --volumes-from myjenkins -v $(pwd):/backup ubuntu tar cvf /backup/backup.tar /dbdata
