FROM jenkins/jenkins:2.204.1-jdk11

# This will skip the wizard UI configuration so you can use Jenkins straight. 
ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false
USER root
# Install docker.
# This image won't be able to perform docker build unless you mount the docker host configuration
RUN apt-get update
RUN apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common \
    python3-pip 
RUN echo 1
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"
RUN apt-get update
RUN apt-get install -y docker-ce docker-ce-cli containerd.io
RUN apt-cache madison docker-ce
ENV VERSION_STRING=18.06.1~ce~3-0~debian
RUN apt-get install -y docker-ce
# This will install the plugins from "plugins.txt"
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt





