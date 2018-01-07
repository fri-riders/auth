FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/auth-1.0-SNAPSHOT.jar /app

EXPOSE 8089

CMD ["java", "-jar", "auth-1.0-SNAPSHOT.jar"]
