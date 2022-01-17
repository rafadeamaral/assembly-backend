FROM openjdk:11
VOLUME /tmp
EXPOSE 8082
ADD ./target/assembly-consumer*.jar assembly-consumer.jar
ENTRYPOINT ["java", "-jar", "/assembly-consumer.jar"]
