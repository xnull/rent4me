sudo apt-get install postgresql-9.3 && sudo apt-get install postgresql-contrib-9.3 && sudo apt-get install postgresql-9.3-postgis-2.1

sudo su - postgres

change in /etc/postgresql/9.3/main/postgresql.conf:
listen_addresses = '*'

add in /etc/postgresql/9.3/main/pg_hba.conf:
host all all 0.0.0.0/0 md5

sudo /etc/init.d/postgresql restart

configuration of users & extensions:

$ psql

> CREATE ROLE gis_group NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;
> CREATE ROLE gis LOGIN PASSWORD 'password' NOINHERIT;
> GRANT gis_group TO gis;
> \q


$ createdb -O gis test_gisdb

createdb -O gis gisdb

$ psql --dbname=gisdb

 > CREATE EXTENSION postgis;
 > CREATE EXTENSION postgis_topology;
 > CREATE EXTENSION btree_gist;

> create extension "uuid-ossp";
> create extension pg_trgm;
> CREATE extension pg_stat_statements;