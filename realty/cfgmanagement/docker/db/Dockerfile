FROM postgres:9.3

RUN echo "doker file!!!"

RUN echo deb http://http.debian.net/debian wheezy-backports main >> /etc/apt/sources.list
RUN apt-get -y update

RUN apt-get install -y ca-certificates rpl pwgen
RUN apt-get install -y postgresql-contrib-9.3 postgresql-9.3-postgis-2.1 postgis

ADD init-database.sh /docker-entrypoint-initdb.d/

RUN echo "this is the end"
