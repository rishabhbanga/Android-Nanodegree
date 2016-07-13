# TNIMDb

Here is a fully functional and colorful android app which I made from scratch for Android Developer Nanodegree program.
This app reveals the power of adaptive UI both for phone and tablet devices.

*TNIMDb* was highly evaluated by certified Udacity code reviewer

## Features

With this app, you can:
* Discover movies based on Popularity, Highest Rated and Favorites
* Save favorite movies locally to view them even when offline
* Watch trailers and Read reviews

## How to Work with the Source

This app uses [The Movie Database](https://www.themoviedb.org/documentation/api) API to retrieve movies
You must provide your own API key in order to build the app. When you get it, just paste it to:
    ```
    app/build.gradle
    ```
    
## Libraries Used

1. ButterKnife (v8.1.0) for view injection
2. Piccaso (v2.5.2) for caching and dowloading the image from the web
3. Retrofit (v2.1.0) for fetching data from the web and simplifying networking code
4. Retrofit Converter Gson (v2.0.0-beta2)
5. Otto (v1.3.8) To decouple different parts of the application

## Android Developer Nanodegree
[![Udacity][1]][2]

[1]: ../master/art/nanodegree-logo.png
[2]: https://www.udacity.com/course/android-developer-nanodegree--nd801

## License

    Copyright 2016 Rishabh Banga

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

