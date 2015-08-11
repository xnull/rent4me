#!/usr/bin/env bash

echo before_kill
docker kill rent
docker stop rent
docker rm rent
echo ater_kill

docker build --rm -t rent4me/db:v1 .
docker --log-level=debug run --name=rent -d -p 5432:5432 rent4me/db:v1

sleep 30
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION postgis;"
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION postgis_topology;"
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION btree_gist;"
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION \"uuid-ossp\";"
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION pg_trgm;"
psql -h localhost -U postgres -d realty_testdb --command "CREATE EXTENSION pg_stat_statements;"
