#!/bin/sh

ROOTDIR=`pwd`
CONFDIR="${ROOTDIR}/conf"
LIBDIR="${ROOTDIR}/lib"

JAVA_OPTS="-Xmx4G"

cmd="java -jar -server ${JAVA_OPTS} \
	-Djava.net.preferIPv4Stack=true \
	-Dfile.encoding=UTF-8 \
	-Dlogback.configurationFile=${CONFDIR}/logback.xml \
	-Dconfig.file=${CONFDIR}/graphene.conf \
	${LIBDIR}/${project.artifactId}-${project.version}.jar"

exec ${cmd}
