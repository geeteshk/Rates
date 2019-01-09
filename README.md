# Rates #

An application that allows you to look at current and historical exchange rates using the OpenRates API. 

# Setup and Build #

It is recommended to use Android Studio to build this project. Open Android Studio and choose 'Open existing Android Studio Project' and select this project directory. Android Studio will proceed to import the files. Next just run using the green triangle at the top or issue the command: 

`./gradlew assembleDebug`

> You may be met with errors since you are missing some dependencies. Simply download them using the error prompt and this should fix this.

# Built With #

* Kotlin
* Retrofit
* RxJava
* Dagger 2
* Android Jetpack (using AndroidX)
  * Data Binding
  * Architecture Components
  * AppCompat
  * Google Material Components Library or something. Not sure what to call this...
* [CircleImageView](https://github.com/hdodenhof/CircleImageView)

Special thanks to my girlfriend, without whom this app would not have been completed. :heart:

# License #

This app is distributed under the Apache License, Version 2.0. See [LICENSE](https://github.com/geeteshk/Rates/blob/master/LICENSE).