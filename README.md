# Technologies used
1. Spring boot (Backend)
2. Spring batch
3. Spring websocket
4. maven
5. Kannel sms gateway (Hosted on ubuntu 16.04 lts)
6. Angular 5 (Frontend)

### Installation
```sh
$ git clone https://github.com/sanskarsakya/kannel_gateway.git
```
### Frontend (Angular)

```sh
$ cd frontend
$ npm install
$ ng serve (localhost:4200)
```

### backend

```sh
$ cd smsfinal
$ mvn clean install
$ mvn spring-boot:run (make sure mysql is active)
```

### MySQL configuration
1. cd src/main/resources/application.properties
2. change as per requirement.


### Important endpoints

| Endpoint (localhost:8080) | Url |
| ------ | ------ |
| / | default |
| /send | triggers send message batch job |
| /post | uploads csv |
| /job | perform insert batch |



**Note: screenshot availale [here](https://github.com/sanskarsakya/kannel_gateway/blob/master/screenshot/Untitled.png)**
