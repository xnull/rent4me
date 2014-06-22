sudo apt-get install postgresql-9.3 && sudo apt-get install postgresql-contrib-9.3 && sudo apt-get install postgresql-9.3-postgis-2.1

sudo su - postgres

change in /etc/postgresql/9.3/main/postgresql.conf:
listen_addresses = '*'

add in /etc/postgresql/9.3/main/pg_hba.conf:
host all all 0.0.0.0/0 md5

sudo /etc/init.d/postgresql restart

configuration of users & extensions:

$ psql

> CREATE ROLE realty_test_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;
> CREATE ROLE realty_test_user LOGIN PASSWORD '<password>' NOINHERIT;
> GRANT realty_test_group TO realty_test_user;
> \q


$ createdb -O realty_test_user realty_testdb

$ psql --dbname=realty_testdb

 > CREATE EXTENSION postgis;
 > CREATE EXTENSION postgis_topology;
 > CREATE EXTENSION btree_gist;
