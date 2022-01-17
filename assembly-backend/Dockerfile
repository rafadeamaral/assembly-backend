FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/assembly*.jar assembly.jar
ENTRYPOINT ["java", "-jar", "/assembly.jar"]
