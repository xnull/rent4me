FROM ubuntu:14.04
MAINTAINER Bynull <xrw.null@gmail.com>

# http://www.howtogeek.com/196635/an-official-google-drive-for-linux-is-here-sort-of-maybe-this-is-all-well-ever-get/

RUN apt-get update
#RUN apt-get install -y golang git mercurial libsasl2-dev
#RUN DEBIAN_FRONTEND=noninteractive  apt-get install -y python-software-properties software-properties-common
#RUN add-apt-repository ppa:duh/golang
#RUN apt-get update
RUN apt-get install -y golang git mercurial

VOLUME /var/google-drive

#go settings
RUN mkdir /go

ENV GOPATH /go
ENV PATH "$PATH:$GOPATH/bin"

# rent4me.storage@gmail.com
# https://github.com/odeke-em/drive
RUN echo "Clone gdrive repository..."
RUN go get -u github.com/odeke-em/drive/cmd/drive

ENV GOOGLE_API_CLIENT_ID "354790962074-7rrlnuanmamgg1i4feed12dpuq871bvd.apps.googleusercontent.com"
ENV GOOGLE_API_CLIENT_SECRET "4/Qj5MAjeABS0AtMvOcM5LSNQ-CRiMpr1utPHDTvla3wE.MhzCM3gK3F8VPvB8fYmgkJxDQfR8mAI"

WORKDIR /var/google-drive
COPY drive/.gd /var/google-drive/.gd
RUN cat /var/google-drive/.gd/credentials.json

RUN yes | /go/bin/drive pull

RUN chmod 777 /var/google-drive/
