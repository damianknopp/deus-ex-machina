#!/bin/bash

PEM=~/.ssh/tribeknoppmicro.pem
MACHINE=ec2-54-196-38-7.compute-1.amazonaws.com
SERVER_USER=ec2-user

scp -i $PEM target/*tar.gz $SERVER_USER@$MACHINE:.
