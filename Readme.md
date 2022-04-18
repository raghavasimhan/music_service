# Coding challenge - Music Service
## Assignment
We want you to create a REST API for providing clients with information about a specific music artist. The information is collected from 4 different  sources:
- MusicBrainz
- Wikidata
- Wikipedia
- Cover Art Archive.

## Requirements
Your API service should be production-ready and be able to handle heavy loads. Assume that your service will have tens of thousands of  requests per second at peak times, which might be a bit of a challenge since your API relies on 3rd party APIs (MusicBrainz, Wikidata, Wikipedia  and Cover Art Archive). Think about availability, response times, resilience, exception handling, testability etc.

## Solution

- So based on the requirement i thought reactive spring would be good choice to go with in terms os performace and flow wise.
- Also the database is used to store information after the first fetch so the subsequent calls becomes faster
- I have added the unit level test cases. (sorry i could find time to add a E2E integration test with emebeeded DB)
- Please find the attached code flow of the logic

### Few Assumption
- when CoverArt call fails setting the imageurl to be empty
- Mapping of data model w.r.t whats need for the assigment
-
## Tech Stack
- Reactive Spring
- Kotlin
- Mongo DB

## More improvements
- More error handling
- Caching improvments so even DB calls can be avoided
- More integration test
- better data model handling

## Installation

#### Building for source

For production release:

```sh
cd plug_surfing
./gradlew clean build
```
## Docker
Due to the Mongo Db integration there is requirement to start the DB before running the app

By default, the Docker will expose port 27017,
```sh
docker run -d  --name mongo-on-docker  -p 27017:27017 mongo
```
#### Running the app
```sh
./gradlew bootrun
```

Verify the deployment by navigating to your server address in
your preferred browser.

```sh
http://localhost:8080/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e
```
