#!/bin/sh
COMMIT = bcc9983237b71379d7024fe2a0550c20276103ad

set -ex
git clone git@github.com:Lambda-3/DiscourseSimplification.git
cd DiscourseSimplification
git checkout bcc9983237b71379d7024fe2a0550c20276103ad
mvn install -B -DskipTests && cd ..
rm -rf DiscourseSimplification
