# Rates #

An application that allows you to look at current and historical exchange rates using the OpenRates API, built with the MVVM pattern. 

# Usage #

Select a base currency and enter your desired amount to see it's value in other supported currencies. Historical rates up until 1999 are also supported thanks to the API. Simply enter your desired date to also view these. The application requires a working internet connection to first fetch data from the API. After doing so one time for the given date and base currency, the app will cache the data using the Room database as to not use internet for obtaining the same data.

# Setup and Build #

It is recommended to use Android Studio to build this project. Open Android Studio and choose 'Open existing Android Studio Project' and select this project directory. Android Studio will proceed to import the files. Next just run using the green triangle at the top or issue the command: 

`./gradlew assembleDebug`

> You may be met with errors since you are missing some dependencies. Simply download them using the error prompt and this should fix this.

# Built With #

* Kotlin
* Retrofit
* [OpenRates](http://openrates.io/)
* RxJava
* Dagger 2
* Android Jetpack (using AndroidX)
  * Room
  * Data Binding
  * Architecture Components
  * AppCompat
  * Google Material Components Library or something. Not sure what to call this...
* [CircleImageView](https://github.com/hdodenhof/CircleImageView)

Special thanks to my girlfriend, without whom this app would not have been completed. :heart:

# Issues/TODO #

* No tests, very important, should write, please hire me Google
* The UI isn't very user friendly or intuitive, needs work
* Need to integrate automated CI builds with Travis
* A couple of redundant code and methods remain

# License #

This app is distributed under the Apache License, Version 2.0. See [LICENSE](https://github.com/geeteshk/Rates/blob/master/LICENSE).
