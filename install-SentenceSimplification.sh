#!/bin/sh
VERSION=5.0.0

set -ex
wget https://github.com/Lambda-3/SentenceSimplification/archive/v$VERSION.tar.gz
tar xfa v$VERSION.tar.gz
cd SentenceSimplification-$VERSION && mvn install -B -DskipTest
