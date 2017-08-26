# Write an application that shows time in different timezones


## Functional requirements
* User must be able to create an account and log in.
* When logged in, a user can see, edit and delete timezones he entered.
* Implement at least three roles with different permission levels: a regular user would only be able to CRUD on their owned records, a user manager would be able to CRUD users, and an admin would be able to CRUD all records and users.
* When a timezone is entered, each entry has a Name, Name of the city in timezone, the difference to GMT time.
* When displayed, each entry also has current time.
* Filter by names.

## Non-functional requirements

* REST API. Make it possible to perform all user actions via the API, including authentication (If a mobile application and you don’t know how to create your own backend you can use Firebase.com or similar services to create the API).
In any case, you should be able to explain how a REST API works and demonstrate that by creating functional tests that use the REST Layer directly. Please be prepared to use REST clients like Postman, cURL, etc. for this purpose.
* All actions need to be done client side using AJAX, refreshing the page is not acceptable. (If a mobile app, disregard this).
You will not be marked on graphic design, however, do try to keep it as tidy as possible.
* Bonus: unit and e2e tests!


NOTE: Please note that this is the project that will be used to evaluate your skills. The project will be evaluated as if you were delivering it to a customer. We expect you to make sure that the app is fully functional and doesn’t have any obvious missing pieces. The deadline for the project is 2 weeks from today.

Please use this private repository to version-control your code:
http://git.toptal.com/jscida/project-grzegorz-gurgul 
Username: grzegorz.gurgul

The project deadline is up to two weeks, but we do encourage you to schedule a time now. Once you schedule a call, we will email you the meeting details, and you will get a technical interviewer assigned. This technical interviewer will be able to answer any questions you might have.


## Implementation

### Client


###### Useful links
* [Grid system](https://github.com/angular/flex-layout/wiki/Declarative-API-Overview)
* [Lodash](https://lodash.com/docs/4.17.4)
* [RxJS](http://reactivex.io/rxjs/class/es6/Observable.js~Observable.html)
* [Material Design components for Angular 4](https://material.angular.io/components)
* [Angular 4 starter project](https://github.com/AngularClass/angular-starter)
    * [Angular 4 examples](https://github.com/AngularClass/angular-examples)
    * [Angular 4 forms](http://blog.ng-book.com/the-ultimate-guide-to-forms-in-angular-2/)
    * [HTTP & Observables](https://codecraft.tv/courses/angular/http/http-with-observables/)

### Server

###### Useful links
* [Application events](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2)
* [Mailserver](https://github.com/tomav/docker-mailserver/wiki/Setup-docker-mailserver-using-the-script-setup.sh)
* [DBSetup](http://dbsetup.ninja-squad.com/user-guide.html)
* [SASS Tester](https://www.sassmeister.com/)
* [Spring DATA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
    * [Examples](https://github.com/spring-projects/spring-data-examples)
* [Spring DATA REST](https://docs.spring.io/spring-data/rest/docs/current/reference/html/)
* [JWT](https://jwt.io/)
