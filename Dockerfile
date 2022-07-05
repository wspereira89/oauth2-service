FROM openjdk:8-alpine
VOLUME /tmp
EXPOSE 9100
ADD ./target/oauth-service.jar  oauth-service.jar
ADD ./config/docker config
ENTRYPOINT [ "java","-jar","oauth-service.jar" ]