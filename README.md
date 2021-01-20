# Budapest Weather :sunny: :cloud:

An Android weather application implemented using the MVVM pattern, HiltRetrofit2, LiveData, ViewModel, Coroutines, Room, Navigation Components, Data Binding and some other libraries from the [Modern Android Development](https://developer.android.com/modern-android-development) blueprint. 

Budapest Weather fetches data from the [MetaWeather API](https://www.metaweather.com/api/) to provide an up to date forecast for the current day. 

## Features

The purpose of the application is to showcase the recent architecture best practices. This is why it is not focusing on the UI - currently
* Shows the current temperature in Celsius, the name and icon of the current weather state and the age of the data shown.
* Caches of the results and reuse it when the app is opened. It becomes invalid after 1 minute from the last update.
* Ability to refresh the result from the remote API with a swipe to refresh gesture.
* On configuration change (e.g. when the user rotates the screen) it does not do I/O  operations, it uses the in-memory version of the already queried data.


## Technologies used:

* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android.
* [Hilt](https://dagger.dev/hilt/) for dependency injection.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) a single-activity architecture to handle all navigations and also passing of data between destinations.
* [Glide](https://bumptech.github.io/glide/) for image loading.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) used to manage the local storage i.e. `writing to and reading from the database`. Coroutines help in managing background threads and reduces the need for callbacks.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to declaratively bind UI components in layouts to data sources.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Android KTX](https://developer.android.com/kotlin/ktx) provide concise, idiomatic Kotlin to Jetpack, Android platform, and other APIs.

## LICENSE
```
MIT License

Copyright (c) 2021 János Kernács

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
