FROM openjdk:8-jre

WORKDIR /usr/share/graphene

# basic functionality
ENV LANG="en_US.UTF-8" TZ="Europe/Berlin"

EXPOSE 8080

# add folder

ADD graphene-server/target/graphene-server-${project.version}-dist.tar.gz .

CMD ["bin/start.sh"]
