#!/usr/bin/env bash

echo before_kill
docker kill you
docker stop you
docker rm you
echo ater_kill

docker build --rm -t rent4me/db:v1 .
docker --log-level=debug run --name=you -d -p 5432:5432 rent4me/db:v1

sleep 30
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION postgis;"
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION postgis_topology;"
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION btree_gist;"
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION \"uuid-ossp\";"
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION pg_trgm;"
psql -h localhost -U postgres -d realty_devdb --command "CREATE EXTENSION pg_stat_statements;"
