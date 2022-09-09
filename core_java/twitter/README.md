# Introduction

The Java Twitter Application utilizes Twitter REST API to post,show or delete a tweet. The application uses command line to take the user input, process the request in MVC architecture and sends the request to the Twitter using Twitter REST API. The application was developed using SpringBoot, Maven, Twitter REST API, HTTP Client. It was deployed using Docker.

# Quick Start
- ## Packaged the application using the below command
```
mvn clean package
```
- ## Run the application with docker using the below commands

Pull the docker image
```
docker pull priyasarma14/grep
```
 Before using the application, set the environment variables with your twitter developer account keys and then run the application.
```
export consumerToken=[yourConsumerToken]
export consumerSecret=[yourConsumerSecret]
export accessToken=[yourAccessToken]
export tokenSecret=[yourTokenSecret]

docker run --rm -e CONSUMERKEY=${consumerKey} -e CONSUMERSECRET=${consumerSecret} -e ACCESSTOKEN=${accessToken} -e TOKENSECRET=${tokenSecret} priyasarma14/twitter post|show|delete [options]

Usage:
-Options to post a tweet:
"post" "tweet_text" "latitude:longitude"
-Options to show a  tweet:
"show" "tweet_id" "[field1,fields2, ...]"
-Options to delete a tweet:
"delete" "[id1,id2,..]" 
```


# Design
## UML diagram
![Twitter project UML diagram](./assets/Twitter_UMLdiagram.drawio)
## App/main
This component receives the user input and sends the arguments to the specific Controller methods based on the post/show/delete request. 
## Controller 
The Controller layer parses and validates the user input for e.g. number of arguments in post, show, and delete tweet; if the text is empty in post tweet request, etc. After that, it sends the request to the specific Service class methods.
## Service 
The Service layer handles the business logic of the application like the tweet cannot exceet 140 characters, if the input ID is in correct format, etc. It will then pass the values to the DAO class methods.
## DAO 
The DAO layer constructs Twitter REST API URIs and make HTTP calls using HttpHelper class.
## Model
Talk about tweet model
The tweet object is a simplified version of the Twitter's tweet object. It consists of the following fields:
```
    created_at String,
    id String,
    id_str String,
    text String,
    entities Entities,
    coordinates Coordinates,
    retweet_count int,
    favorite_count Integer,
    favorited Boolean,
    retweeted Boolean
```
## Spring
Three different approaches were taken to manage the dependencies using Spring. 
- @Beans approach
The beans have been manually created and informed to the Spring framework that it can receive the beans from the methods having the @Beans annotation before the methods.

- @ComponentScan approach
In this approach, instead of manually creating the objects, I have used the @ComponentScan to find the Beans through the specified packages and inject the dependencies.

- @SpringBootApplication approach
 TwitterCLISpringBoot class uses @SpringBootApplication, which is a composition of multiple annotations which help to configure Spring automatically. This class also implements the CommandLineRunner interface so that the application can run with the command line arguments.

# Test
I have manually tested the application as well as used JUnit and Mockito for testing the application. Integration testing was done for all of the components using JUnit. Unit testing was also done for all of the components using Junit and Mockito. Mockito was used to mock the dependencies and the behavior of the components so that the components can be tested in isolation.

## Deployment
I have deployed the application using docker. Below are the steps taken for the deployment:
```
1. Created the dockerfile.
2. Created the uber jar using maven.
3. Built a new docker image locally
   docker build -t ${docker_user}/twitter.
4. Verified the image created.
   docker image ls | grep "twitter"
5. Ran the docker image.
6. Pushed the docker image to DockerHub.
   docker push ${docker_user}/twitter
```

# Improvements
- Allowing users to search a posted tweet with the text also.
- Creating a GUI for the application.
- Allowing users to view all of their posted tweets.
