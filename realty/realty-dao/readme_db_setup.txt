sudo apt-get install postgresql-9.3 && sudo apt-get install postgresql-contrib-9.3 && sudo apt-get install postgresql-9.3-postgis-2.1

sudo su - postgres

change in /etc/postgresql/9.3/main/postgresql.conf:
listen_addresses = '*'

add in /etc/postgresql/9.3/main/pg_hba.conf:
host all all 0.0.0.0/0 md5

sudo /etc/init.d/postgresql restart

configuration of users & extensions:

From command line run following commands:


sudo su - postgres


#create test database
psql -c "CREATE ROLE realty_test_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;"
psql -c "CREATE ROLE realty_test_user LOGIN PASSWORD 'password' NOINHERIT;"
psql -c "GRANT realty_test_group TO realty_dev_user"

createdb -O realty_test_user realty_testdb

 psql --dbname=realty_testdb -c "CREATE EXTENSION postgis;"
 psql --dbname=realty_testdb -c "CREATE EXTENSION postgis_topology;"
 psql --dbname=realty_testdb -c "CREATE EXTENSION btree_gist;"
 psql --dbname=realty_testdb -c "create extension \"uuid-ossp\";"
 psql --dbname=realty_testdb -c "create extension pg_trgm;"
 psql --dbname=realty_testdb -c "CREATE extension pg_stat_statements;"


#create local dev database
psql -c "CREATE ROLE realty_dev_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;"
psql -c "CREATE ROLE realty_dev_user LOGIN PASSWORD 'password' NOINHERIT;"
psql -c "GRANT realty_dev_group TO realty_dev_user"

createdb -O realty_test_user realty_devdb

 psql --dbname=realty_devdb -c "CREATE EXTENSION postgis;"
 psql --dbname=realty_devdb -c "CREATE EXTENSION postgis_topology;"
 psql --dbname=realty_devdb -c "CREATE EXTENSION btree_gist;"
 psql --dbname=realty_devdb -c "create extension \"uuid-ossp\";"
 psql --dbname=realty_devdb -c "create extension pg_trgm;"
 psql --dbname=realty_devdb -c "CREATE extension pg_stat_statements;"



--- create production database
psql -c "CREATE ROLE realty_prod_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;"
psql -c "CREATE ROLE realty_prod_user LOGIN PASSWORD '<PRODUCTION_PASSWORD>' NOINHERIT;"
psql -c "GRANT realty_prod_group TO realty_prod_user"

createdb -O realty_prod_user realty_proddb

 psql --dbname=realty_proddb -c "CREATE EXTENSION postgis;"
 psql --dbname=realty_proddb -c "CREATE EXTENSION postgis_topology;"
 psql --dbname=realty_proddb -c "CREATE EXTENSION btree_gist;"
 psql --dbname=realty_proddb -c "create extension \"uuid-ossp\";"
 psql --dbname=realty_proddb -c "create extension pg_trgm;"
 psql --dbname=realty_proddb -c "CREATE extension pg_stat_statements;"