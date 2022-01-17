FROM openjdk:11
VOLUME /tmp
EXPOSE 8081
ADD ./target/assembly-producer*.jar assembly-producer.jar
ENTRYPOINT ["java", "-jar", "/assembly-producer.jar"]
