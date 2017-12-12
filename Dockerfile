FROM java:8-jre

ADD ./target/oap-sample.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/oap-sample.jar"]

EXPOSE 8000