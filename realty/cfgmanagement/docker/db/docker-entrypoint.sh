#/bin/bash

echo "------- Postgre init scrit ---------"

psql -c "CREATE ROLE realty_test_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;"
psql -c "CREATE ROLE realty_test_user LOGIN PASSWORD 'password' NOINHERIT;"
psql -c "GRANT realty_test_group TO realty_dev_user"

