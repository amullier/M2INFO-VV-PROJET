#!/bin/bash
projectName="M2INFO-VV-DUMMY-PROJET"
rm -rf TargetProject > /dev/null
mkdir TargetProject > /dev/null
cd TargetProject 
echo "Récupération du projet depuis GIT"
git clone "https://github.com/amullier/$projectName"
mv ${projectName}/* ./
rm -rf $projectName 

mvn clean install
