# Article Generator
Application for generating articles using artificial intelligence  
User generation request contain tags. Tags used to generate topics. Topic used to generate article
## Getting Started
### Installation
1. Clone the repo  
```git clone https://github.com/Daniil-java/article_generator.git``` 
2. Run docker-compose.yaml file or use next command in command line  
```docker-compose up -d```
3. Enter your API key in database table `article_generator.open_ai_key` like `ArticleKey` and `ArticleTopicKey`
4. The application is ready to use
### Usage
* Generation request creating. To create a request, you must make a post request at:  
```http://localhost:8110/api/v1/generationrequest/```.   
In the body of the request, you need to specify the tags or topics of the generated article. 
For example, the "programming" tag will be used.  
Json-body:  
```{"requestTags" : "programming"}```  
  This is enough for the service to start automatically generating articles using schedulers.
  To manually control the service, you need to send 2 more get requests.
* Article topic creating/getting. To create or get a topic, you must make a get request at:  
  ```http://localhost:8110/api/v1/articletopic?requestId={Id of your generation request}```.
* Article creating/getting. To create or get a article, you must make a get request at:  
  ```http://localhost:8110/api/v1/article?topicIds={Id of your topic}```.
### Swagger
You can use SWAGGER in URL:
```http://localhost:8110/swagger-ui/index.html```

