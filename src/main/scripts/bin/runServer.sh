#!/bin/bash

DEUSEXMACHINA_ENCRYPTION_PASSWORD=xxxx

CP=./
for jar in `ls lib/*.jar`
do
  CP="$CP:$jar"
done

echo "java -cp $CP -DDEUSEXMACHINA_ENCRYPTION_PASSWORD=$DEUSEXMACHINA_ENCRYPTION_PASSWORD dmk.deusexmachina.cli.AntikytheraMechanismDaemonCLI"
java -cp $CP -DDEUSEXMACHINA_ENCRYPTION_PASSWORD=$DEUSEXMACHINA_ENCRYPTION_PASSWORD dmk.deusexmachina.cli.AntikytheraMechanismDaemonCLI