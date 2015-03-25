# social-crawler-yelp-adapter
Fetches yelp comments and imports them to SQS. AWS and api configuration is in ```src/main/resources/application.conf```

# Run
```
sbt run
```

# Build fat-jar file and run
```
sbt assembly
java -jar JARFILENAME.jar
```
