FROM maven:3.8.5-openjdk-17-slim

WORKDIR /usr/src/app/back
COPY . .
EXPOSE 8090
CMD mvn spring-boot:run