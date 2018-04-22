# Technologies used
1. Spring boot (Backend)
2. Spring batch
3. Spring JPA
4. Spring websocket
5. maven
6. Kannel sms gateway (Hosted on ubuntu 16.04 lts)
7. Angular 5 (Frontend)

### prerequisite
1. maven
2. node
3. java JDK


![alt text](https://github.com/sanskarsakya/kannel_gateway/blob/master/screenshot/Untitled.png "kannel test")


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

### Backend

```sh
$ cd smsfinal
$ mvn clean install
$ mvn spring-boot:run (make sure mysql is active)
```

### MySQL configuration
1. cd src/main/resources/application.properties
2. change as per requirement.


### Important endpoints

| Endpoint (localhost:8080) | Description |
| ------ | ------ |
| / | default |
| /send | triggers send message batch job |
| /post | uploads csv |
| /job | perform insert batch |



**Note: screenshot available [here](https://github.com/sanskarsakya/kannel_gateway/blob/master/screenshot/Untitled.png)**

![alt text](https://github.com/sanskarsakya/kannel_gateway/blob/master/screenshot/kannel_status.PNG "kannel status")
