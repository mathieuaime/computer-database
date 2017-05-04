FROM mysql:5.5.54
      
ENV MYSQL_ROOT_PASSWORD=root

RUN apt-get update \
      && apt-get install -y sudo nano

ADD sqlScript/1-SCHEMA.sql /docker-entrypoint-initdb.d
ADD sqlScript/2-PRIVILEGES.sql /docker-entrypoint-initdb.d
#ADD sqlScript/insertCompany.sql /docker-entrypoint-initdb.d
#ADD sqlScript/insertComputer.sql /docker-entrypoint-initdb.d


COPY sqlScript/insertCompany.sql /usr/share/
COPY sqlScript/insertComputer.sql /usr/share/