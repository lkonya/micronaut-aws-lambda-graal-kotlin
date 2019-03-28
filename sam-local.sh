#!/bin/sh
docker build . -t aws-lambda-graal-kotlin
mkdir -p build
docker run --rm --entrypoint cat aws-lambda-graal-kotlin  /home/application/function.zip > build/function.zip

sam local start-api -t sam.yaml -p 3000

