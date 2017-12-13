#!/bin/bash
RED='\033[1;31m'
GREEN='\033[0;32m'
LIGHT_GREEN='\033[0;32m'
BLUE='\033[0;34m'
LIGHT_BLUE='\033[1;34m'
ORANGE='\033[0;33m'
NC='\033[0m'

url="https://github.com/amullier/M2INFO-VV-DUMMY-PROJET"

function print_usage()
{
	echo "$BLUE $0 $NC -d [DEST_DIR]"
	echo "${BLUE}DEST_DIR :$NC Dossier de destination du projet"
}

rm -rf M2INFO-VV-DUMMY-PROJET > /dev/null

echo "Récupération des sources depuis Github"
git clone "$url"
cd M2INFO-VV-DUMMY-PROJET

echo "Compilation des sources à l'aide de MAVEN"
mvn clean install > /dev/null

echo "Importation du projet de test terminée."
