FROM java:8 

# Install maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
ADD test /code/test
#RUN ["mvn", "package"]

EXPOSE 4567
#CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/computer-database-0.3.0-SNAPSHOT.war"]
ENTRYPOINT ["mvn","test"]