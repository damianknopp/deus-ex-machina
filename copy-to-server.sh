#!/bin/bash

PEM=~/.ssh/tribeknoppmicro.pem
MACHINE=$1
SERVER_USER=ec2-user

scp -i $PEM target/*tar.gz $SERVER_USER@$MACHINE:.
