FROM maven:3.8-openjdk-17

WORKDIR /app

ARG VERSION

ADD . ./

RUN sed -i -E 's/(\ *<version>)dev(<\/version>)/\1'"$VERSION"'\2/' pom.xml

# generate jar
RUN mvn package

RUN mv target/piService-*.jar app.jar

CMD java -jar ./app.jar
