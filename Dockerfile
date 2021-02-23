FROM ubuntu:18.04
WORKDIR /app
RUN apt-get update
RUN apt-get install -y git
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y maven