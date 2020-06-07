FROM openjdk:11

ADD ./target/simple-file-storage-1.0-SNAPSHOT.jar /usr/app/sfs.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/app/sfs.jar"]