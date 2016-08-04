# TNIMDb

TNIMDb is a fully functional and colorful android app which I made from scratch for the Android Developer Nanodegree program.

This app reveals the power of adaptive UI both for phone and tablet devices. 

Movies along with their subsequent Ratings, Comments and Trailers can be viewed by the following three categories:

1. Popular
2. Top Rated
3. Favorites (Selected by User)

*TNIMDb* was highly evaluated by a certified Udacity code reviewer with the following message:

```
Great!

You have done a superb job so far! 

I was really impressed by what you had built! As a user myself, I really enjoyed using your app! 

Keep up the good work  
```

## Features

With this app, you can:
* Discover movies based on Popularity, Highest Rated and Favorites
* Save favorite movies locally to view them even when offline
* Watch trailers and Read reviews

## How to Work with the Source

This app uses [The Movie Database](https://www.themoviedb.org/documentation/api) API to retrieve movies
You must provide your own API key in order to build the app. When you get it, just paste it inside:
    ```
    app/build.gradle
    ```
    
## External Libraries Used

1. ButterKnife (v8.1.0) for view injection
2. Retrofit (v2.1.0) for fetching data from the web and simplifying networking code
4. Retrofit Converter Gson (v2.0.0-beta2)
5. Piccaso (v2.5.2) for caching and dowloading the image from the web
6. Otto (v1.3.8) to decouple different parts of the application
7. Selectableroundedimageview (v1.0.1) to integrate ImageView that supports different radii on each corner
8. Stetho (v1.2.0) for sophisticated debugging
9. Timber (v4.1.0)

## ScreenShots

#### Categorized Grid view
![alt img](https://github.com/rishabhbanga/Android-Nanodegree/blob/master/screenshots/tnimdb.png)

#### Detail view
![alt img](https://github.com/rishabhbanga/Android-Nanodegree/blob/master/screenshots/movie_detail.png)