# README #

### What is this repository for? ###

This is a simple REST microservice to accomplish recruitment task given by Allegro Team (They didn't look for junior... what i have learned AFTER recruitment. So what i have told that i apply on junior at the very beginning).

It allows you to search any GitHub repository by request:

```
GET /repositories/{owner}/{repoName}
```
Response should be like:

```JSON
{
  "fullName": "...",
  "description": "...",
  "cloneUrl": "...",
  "stars": 0,
  "createdAt": "..."
}
```
In case of error, response should look as follows:
```JSON
{
  "message": "..."
}
```
To do it, you can use - for example - [Postman](https://www.getpostman.com/).

GitHub API reference : https://developer.github.com/v3/
 
> **IMPORTANT** header 'Content-Type: application/json;charset=UTF-8' is required.

> *Note also that creationAt field depends on locale:* header 'Accept-Language: *language code*',
> for example 'Accept-Language: pl'.


### How do I get set up? ###
Just type placed below commands in console in order execute given actions:

 Command                  | Task 
 ------------------------ | ------------------------------
 `gradle bootRun`         | Runs application on port 8080
 `gradle test`            | Runs unit tests
 `gradle integrationTest` | Runs integration tests

Gradle and Java 8 required (tested on Gradle 4.0.1 and jdk 8 update 131).
Also you can use included Gradle Wrapper - just replace `gradle` with `gradlew`.
If you don't have Gradle installed, you can also use `./gradlew`.
