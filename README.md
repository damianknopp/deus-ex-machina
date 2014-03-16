Epic login
====================

####dependencies
* _dmk-mail_
* Install [Jasypt binary] (http://www.jasypt.org/download.html)
* Java 7
* Maven

####To build
_mvn clean install_
_mvn clean package single:assembly_

####To install
Given the deus-ex-machina-bin.tar.gz
  gzip -dc deus-ex-machina-bin.tar.gz | tar -xvf -
  cd deus-ex-machina
  edit lib/deus-ex-machina.properties and add ENC properties and email


###To run
  ./bin/runServer.sh
