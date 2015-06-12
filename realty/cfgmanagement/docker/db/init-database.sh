#/bin/bash


echo "------- Postgre init scrit ---------"

ps aux | grep postgres

gosu postgres postgres --single <<- EOSQL
    CREATE ROLE realty_test_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;
    CREATE ROLE realty_test_user LOGIN PASSWORD 'password' NOINHERIT;
    GRANT realty_test_group TO realty_dev_user;
EOSQL
