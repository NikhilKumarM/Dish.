**Abstract**

Dish is an Android based app that enables users to endorse their favorite foods at               
restaurants by providing pictures and comments. The app was created to assist local business              
that may not have strong a strong social media presence or that are unknown local hangouts. It                
also assists users who do not know what the best single dish at a given restaurant is. The app                   
is supported by backend services that are hosted on AWS, this increases availability of the app                
during peak usage times. Information is recorded in a relational database and is readily              
available for retrieval as users explore their current environment. 

**Details**

Users sign in to our app using either their email, google or facebook account.  Users are then able to do two things.  First, users are able to take photos of both the current dish they are enjoying.  When a photo is taken the user has the opportunity to write a comment before associating the image with the current restaurant they are at using Google Places.  Secondly, by using the Google Maps API, the user will be presented with map showing pins at the location of all restaurants within a set radius of them (10 mile).  Once the user selects a restaurant, they are able to view a list of images and comments about dishes at the restaurant.

**Application Flow**

![Alt text](https://github.com/NikhilKumarM/Dish./blob/master/appRelatedImages/Application_Flow.JPG)

**App ScreenShots**

![Alt text](https://github.com/NikhilKumarM/Dish./blob/master/appRelatedImages/food_items_screen.png)
![Alt text](https://github.com/NikhilKumarM/Dish./blob/master/appRelatedImages/maps_screen.png)
![Alt text](https://github.com/NikhilKumarM/Dish./blob/master/appRelatedImages/review_screen.png)
