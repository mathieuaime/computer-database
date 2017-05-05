FROM jenkins
 
USER root
RUN apt-get update \
      && apt-get install -y sudo git\
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

VOLUME /code
 
RUN git clone https://github.com/mathieuaime/computer-database /code