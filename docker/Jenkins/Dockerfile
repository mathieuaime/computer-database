FROM jenkins
 
USER root
RUN apt-get update \
      && apt-get install -y sudo git\
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
 
RUN git clone https://github.com/mathieuaime/computer-database /usr/share/
#USER jenkins
#COPY plugins.txt /usr/share/jenkins/plugins.txt
#RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt