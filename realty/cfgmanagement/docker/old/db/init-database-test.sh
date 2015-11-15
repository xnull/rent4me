#!/bin/sh

#https://registry.hub.docker.com/u/mdillon/postgis/
#https://registry.hub.docker.com/_/postgres/

echo "------------- Init db -----------"

POSTGRES="gosu postgres postgres"

$POSTGRES --single -E <<EOSQL
CREATE DATABASE realty_testdb

CREATE ROLE realty_test_group SUPERUSER CREATEDB CREATEROLE;
CREATE ROLE realty_test_user LOGIN PASSWORD 'password' NOINHERIT;
GRANT realty_test_group TO realty_test_user;
EOSQL

echo "------------ create postgis extention -------------"
#$POSTGRES --single realty_devdb -E <<EOSQL
#CREATE EXTENSION postgis;
#EOSQL