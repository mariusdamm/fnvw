FROM sapmachine:ubuntu-24.04
ARG JAR_FILE=server/target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]