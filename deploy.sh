#!/bin/sh

USER="root"
HOST="localhost"
APP="/usr/local/FileOperator"

if [ -n "$1" ]; then
    USER="$1"
    shift
fi

if [ -n "$1" ]; then
    HOST="$1"
    shift
fi

if [ -n "$1" ]; then
    APP="$1"
fi


echo "-- FILE OPERATOR DEPLOYER         --"
echo "-- Sto per aggiornare il TARGET   --"
echo " "
echo "   USER=$USER"
echo "   HOST=$HOST"
echo "   APP=$APP"
echo " "
echo "-- premi INVIO per continuare     --"
echo "-- oppure Ctrl+c per interrompere --"
echo "-- modo d'uso:                    --"
echo "-- deploy.sh [user] [host] [app] --"
read DUMMY

DDIR1="$APP"

# copia struttura
COPYCMD="rsync -azv --exclude .svn --exclude README.TXT"

set -x
ssh $USER@$HOST "mkdir -p ${DDIR1}/lib; cd ${DDIR1}; rm -rf *.jar lib/*.jar"
$COPYCMD \
  fileOperator.sh \
  target/FileOperator-1.0-SNAPSHOT.jar \
  $USER@$HOST:${DDIR1}

$COPYCMD \
  target/lib/commonlib5-1.0-SNAPSHOT.jar \
  target/lib/commons-io-2.11.0.jar \
  target/lib/java-getopt-1.0.13.jar \
  $USER@$HOST:${DDIR1}/lib
