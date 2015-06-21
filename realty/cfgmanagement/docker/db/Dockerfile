FROM mdillon/postgis

#RUN echo "doker file build !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"

#ENV DEBIAN_FRONTEND noninteractive

#RUN echo deb http://http.debian.net/debian wheezy-backports main >> /etc/apt/sources.list
#RUN apt-get -y update

#RUN apt-get install -y software-properties-common python-software-properties
#RUN apt-get install -y ca-certificates rpl pwgen
#RUN apt-get install -y postgresql-contrib-9.3 postgresql-9.3-postgis-2.1 postgis

#RUN echo "Entry point !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
ADD init-database.sh /docker-entrypoint-initdb.d/

RUN echo "this is the end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
