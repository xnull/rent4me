#!/bin/sh

#https://registry.hub.docker.com/u/mdillon/postgis/
#https://registry.hub.docker.com/_/postgres/

echo "------------- Init db -----------"

POSTGRES="gosu postgres postgres"

$POSTGRES --single -E <<EOSQL
CREATE DATABASE realty_devdb

CREATE ROLE realty_dev_group SUPERUSER CREATEDB CREATEROLE;
CREATE ROLE realty_dev_user LOGIN PASSWORD 'password' NOINHERIT;
GRANT realty_dev_group TO realty_dev_user;
EOSQL

echo "------------ create postgis extention -------------"
#$POSTGRES --single realty_devdb -E <<EOSQL
#CREATE EXTENSION postgis;
#EOSQL