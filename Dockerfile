FROM openjdk:8-jdk-alpine

# Install Maven
RUN apk add --no-cache curl tar bash git
ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DIR="/root"
RUN mkdir -p /usr/share/maven && \
curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ENV LANG="en_US.UTF-8" TZ="Europe/Berlin"

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# Install project dependencies and keep sources
# make source folder
RUN mkdir -p /usr/src/app
RUN mkdir -p /usr/local/graphene

COPY . /usr/src/app/
WORKDIR /usr/src/app

RUN ./install-DiscourseSimplification.sh
RUN mvn -P server clean package -DskipTests

RUN tar -xzf graphene-server/target/graphene-server-*-dist.tar.gz -C /usr/local/graphene/

WORKDIR /usr/local/graphene

RUN rm -r /usr/src/app

CMD ["bin/start.sh"]
