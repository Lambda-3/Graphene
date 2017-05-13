#!/bin/sh
VERSION=5.0.0

set -ex
wget https://github.com/Lambda-3/SentenceSimplification/archive/v$VERSION.tar.gz -O SentenceSimplification.tar.gz
tar xfa SentenceSimplification.tar.gz
cd SentenceSimplification-$VERSION && mvn install -B -DskipTests && cd ..
rm -rf SentenceSimplification.tar.gz
rm -rf SentenceSimplification-$VERSION
