Epic login
====================

####dependencies
* _dmk-mail_
* _dmk-twilio_
* _dmk-mapquest_
* Install [Jasypt binary] (http://www.jasypt.org/download.html)
* Java 8
* Maven

####To build
_mvn clean install_
_mvn clean package assembly:single_

####To install
Given the deus-ex-machina-bin.tar.gz
* gzip -dc deus-ex-machina-bin.tar.gz | tar -xvf -
* cd deus-ex-machina
* edit the bin/run script and add the secret key used to encrypt your ENC properties
* edit lib/deus-ex-machina.properties and add ENC properties


###To run
  ./bin/runServer.sh
