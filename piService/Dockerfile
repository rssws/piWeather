FROM maven:3.8-openjdk-17

WORKDIR /app

ADD . ./

# generate jar
RUN mvn package

RUN mv target/piService-*.jar app.jar

EXPOSE 31415

CMD java -jar ./app.jar
