# Problem Statement

As part of this project, we present to you `Delagram: A Photo Portal`. This is a web-application where users can upload pictures (we like `memory` - we believe every worth picture has an emotion attached with it), that is specifically designed keeping privacy in mind. With the way the software globalisation world has been moving in the past couple of years, it is quite evident that every country wants software independence - from social media applications to vital and crucial security applications. In this direction, we have taken up the task of creating a social media portal specifically for our fellow-mates at IIT-B.

Users of this application can form communities among themselves, while we ensure their security in that they don't inadvertantly include an unknown in their `circle`. With all that has been going with the world, this application is meant to provide a much needed social interaction among people that are close through friendship, and yet (sadly) far by distance, while ensuring their privacy.

`If you like privacy and social interaction among your close friends, Delagram is the place to be!!`

<h3><a href="https://savvysiddharth.github.io/project-delagram/">Quick Links</a></h3>

# List of features

The initial build of the web based application provides the following set of cool functionalities - 

1. Signup for `free` - with no ads disturbing you!
2. Connect with close ones - only people who know your unique (and *private*) `user_id`. See? We weren't kidding about `privacy`!
3. Upload `memories` and cherish them with your `circle` - and yes, we do support `filters`.
4. View `memories` of friends - interact with them (`comment` and/or give a `star`! - all of us could benefit from some dopamine).
5. Chat, or as we call it - `Gossip`. We don't want you to obligate you with discussions on study related stuff that you do on other chat apps (you know which ;) - it even rhymes with 'app').
6. We don't condone customer sticking in our application the whole day - studies are `important`! So, unlike some other applications (strangely they have `book` in their name!), we don't constantly serve you "serendipitous" stuff to keep you hooked.

We hope you enjoy your visits to our application :)

## Backend Architecture:
![backend](https://user-images.githubusercontent.com/12862695/139908044-5fe8e939-15f0-4a91-98fd-59aa92c51f73.png)

## Sign Up Mechanism:
![delagramAuth - signup](https://user-images.githubusercontent.com/12862695/139906571-5c67d4d5-d006-4666-a8da-bad2c4d6f5c7.jpg)

## Login Mechanism:
![delagramAuth - Login](https://user-images.githubusercontent.com/12862695/139906579-d983e475-1fd8-48f2-a531-f759149eee7f.jpg)

## General API Call:
![delagramAuth - general](https://user-images.githubusercontent.com/12862695/139906585-abf62c39-b937-4eca-8f17-7fb871a10ef4.jpg)

## Texting Mechanism:
![delagramAuth - chat](https://user-images.githubusercontent.com/12862695/139906614-16819609-573a-4d04-a6e6-9dc5e0da1892.jpg)

# Contributors

<a href="https://github.com/mayankkakad">Mayank Kakad</a> | 
<a href="https://github.com/savvysiddharth">Siddharth Maurya</a> | 
<a href="https://github.com/swaroop-nath">Swaroop Nath</a>

# Demo Videos

Login and Signup - https://drive.google.com/file/d/1YqpZ-4dMpVolsbeuElA7gn5sFFC8CyqK/view?usp=sharing

Functionalities - https://drive.google.com/file/d/1c8seaa3CC-xFF2b7yM50zxWklZdhoUGx/view?usp=sharing

# Technology Stack

The web-application uses the following technologies - 

1. ReactJS (`html`, `css`, `js`).
2. `Java`, Maven, Spring Boot.
3. `SQLite`.
4. Firebase (authentication, data storage).

# List of deliverables

![100%](https://progress-bar.dev/100) Signup/Login functionality.

![100%](https://progress-bar.dev/100) Image Upload with filters.

![100%](https://progress-bar.dev/100) Feed viewing and interacting with memories - giving stars, commenting and reporting (in grave cases).

![50%](https://progress-bar.dev/50) Privacy limits and updating profile.

![100%](https://progress-bar.dev/100) Direct messaging with people in circle.

# Hardware and Software requirements

Hardware requirements - 

1. `A working computer`

Software requirements - 

1. `Java 11`
2. `NPM`
3. `NodeJS`
4. `Maven`
5. `Spring`

# How to operate

- In order to start the frontend, do the following -

        cd Frontend

        npm install

        npm start

- In order to start the server -  

        cd Backend

        mvn spring-boot:run

Make sure you have the following installed - 

1. Node modules in the `delagram-ui` folder.
2. mvn - for Mac users, just run `brew install mvn`.

# Primary Stakeholders of the project

1. IIT-B students.
2. Team Tensors.

# Team Details

The team is comprised of -

1. Siddharth Nimeshbhai Maurya (213050033)
2. Mayank Kakad (213050028)
3. Swaroop Nath (21Q050014)

Work distribution - 

Given the time distribution, we had two constraints that we aimed to respect - 

1. Play to each of our strong suits.
2. Equitable work distribution.

`Siddharth`, with the pre-exposure to front-end technologies, was a great find for the team.

`Mayank`, with his previous exposure to Android app development and Firebase was another great addition.

`Swaroop`, with his experience in industrial web-app development and mircroservices, was a valuable addition to backend technologies.

Responsibilities for each team mate - 

1. Siddharth - Development of the full UI on React JS, while maintaining a coherence with backend APIs made available.
2. Swaroop - Developement, as well as unit-testing, of the following microservices - 

        content_upload_service

        content_view_service

        profile_search_service

Along with this, this particular member has coded the pipeline of verification of Firebase JWT that comes from the frontend.

3. Mayank - Development, as well as unit-testing, of the following microservices - 

        auth_service
        
        chatting_service (has been a really tricky one)

Along with this distribution, the members have regularly engaged in sprint calls, ensuring that we progress ahead with a working code, tested end-to-end, following the well-established web-design principles.

# Path to documentation

### Backend : https://savvysiddharth.github.io/project-delagram/Backend/doc/
        
        Backend/doc/index.html

### Frontend : https://savvysiddharth.github.io/project-delagram/Frontend/docs/
        
        Frontend/docs/index.html

# Screenshots:

### Login
![00 Login](https://user-images.githubusercontent.com/12862695/138884749-61360e63-176f-49eb-b94f-bd64a39d6c69.png)

### Sign Up
![01 SignUp](https://user-images.githubusercontent.com/12862695/138884759-11fba8e7-2c0a-4caf-808d-365321190a7c.png)

### Memory Card
![02 1 MemoryCard](https://user-images.githubusercontent.com/12862695/138884762-6d218941-e375-4d25-a83e-8ebd48976cdc.png)

### Dashboard
![02 dashboard](https://user-images.githubusercontent.com/12862695/138884768-72d6629a-74b5-4996-bacb-c1ce03d74c53.png)

### Feed
![03 feed](https://user-images.githubusercontent.com/12862695/138884778-096abe78-0303-44f8-9dd5-cfdc605cb531.png)

### Gossip Room
![04 gossip](https://user-images.githubusercontent.com/12862695/138884788-3a2ad4dc-340e-46ec-b3dd-fffe2cbf6bfa.png)

### Creating a Memory
![05 UploadMemory](https://user-images.githubusercontent.com/12862695/138884819-570987a5-4643-4627-ac3b-b043a55381ed.png)

### Add Buddy
![06 addBuddy](https://user-images.githubusercontent.com/12862695/138884828-78bc6bb8-9321-4936-bd67-a4fbfb8d3c07.png)

# References:

1. https://www.npmjs.com/package/instagram-filters
2. https://loading.io/css/
3. https://ionic.io/ionicons
4. https://firebase.google.com/docs/build
5. https://www.baeldung.com
6. https://stackoverflow.com
7. https://www.youtube.com/watch?v=clH7SxG-Vdc&list=PLS1QulWo1RIYMQcf1y2bqQZbpXLzpKDUL
8. https://www.geeksforgeeks.org/mvc-design-pattern/
9. https://www.tutorialspoint.com/spring_boot/spring_boot_sending_email.htm

