#!/bin/sh
# generates swift code from swagger
# for custom yaml pass url to it as a first parameter

custom_url=$1

if [ -z $custom_url ]; then
    custom_url="https://raw.githubusercontent.com/matchmore/alps-api/master/src/main/resources/alps-core.yaml"
fi
wget $custom_url

rm -fr ./Alps
#swagger-codegen generate -l kotlin -Dmodels -Dapis -c swagger-config-kotlin.json -i ./alps-core.yaml -o api/
swagger-codegen generate -l java -c swagger-config-java.json -i ./alps-core.yaml -o gen/

rm -f alps-core.yaml