# Newsster
<img src="/previews/preview_1.gif" align="right" width="33%"/>

Newsster is a demo application that uses MVVM pattern and Android Jetpack: the Pagging 3 library with a backend API, Saved State module for ViewModel, LiveData, ViewBinding, Room, Dagger-Hilt and Navigations Components. The app fetches data from the network with use of Retrofit integrating persisted data in the database via repository pattern. The app also uses and Kotlin Coroutines + Flow. Newsster provides translated text for the other languages, support for a landscape orientation and a night mode. 

## Tech stack & Open-source libraries
### Jetpack components:</b>
  - Paging 3 sets up a Repository that will use the local database to page in data for the UI and also back-fill the database from the network as the user reaches to the end of the data in the database.
  - Room Persistence - Access app's SQLite database with in-app objects and compile-time checks.
  - ViewBinding - allows to more easily write code that interacts with views and replaces ```findViewById```.
  - ViewModel - UI related data holder, lifecycle aware.
  - Saved State module for ViewModel data that survives background process restart.
  - Lifecycles - Create a UI that automatically responds to lifecycle events.
  - LiveData - Build data objects that notify views when the underlying database changes.
  - SafeArgs for navigating and passing data between fragments.
  - Navigation - Handle everything needed for in-app navigation.
  - Room - Access app's SQLite database with in-app objects and compile-time checks.
  - Dagger-Hilt for dependency injection. Object creation and scoping is handled by Hilt.
  
### Third party:
<img src="/previews/preview_2.gif" align="right" width="33%"/>

  - Glide for image loading.
  - Kotlin Coroutines + Flow for managing background threads with simplified code and reducing needs for callbacks
  - Retrofit2 & OkHttp3 - to make REST requests to the web service integrated.
  - Moshi to handle the deserialization of the returned JSON to Kotlin data objects.
  - Timber for logs.
 
### Architecture:
  - MVVM Architecture 
  - Repository pattern
  
## Testing 
###  Device Tests:
  - <b>App Navigation Test</b> - Navigation between screens is tested using Espresso UI framework and ActivityScenario for lifecycle state. `Hilt` provides test version of Repository and automatically generates a new set of components for each test. This is done with use of a `CustomTestRunner` that uses an Application configured with Hilt. In order to make Espresso aware of network operations `UriIdlingResource` is registered for UI test.
  - <b>Database Testing</b> - The project creates an in memory database for each database test but still runs them on the device.
### Local Unit Tests:
  - <b>Webservice Tests</b> - The project uses MockWebServer project to test REST api interactions.
  - <b>ViewModel Tests</b> - ViewModels are tested using local unit tests with mock Repository implementations.
  - <b>Repository Tests</b> - Repository is tested using local unit tests with mock versions of Service and Database.


## Design
+ Newsster is built with Material Components for Android.
+ The app starts with an asymmetric staggered list of news displayed in the RecyclerView widget. The screen also consists of options menu with category item as an icon in the app bar and setting item that appears in the overflow menu and lets the user switch themes and language. The screen takes the user to the article details with a beautiful collapsing layout. 
+ The app has dialogs, buttons, menu and progress indicator customized for colors, shapes and typography. All clickable components behave intuitively changing their appearance when they are pressed.
+ The app has beautiful colors schemes for day and night modes.

## Features
+ Translation for other languages.
+ Picking categories.
+ Opening an article's web site,
+ Sharing and article link,
+ Supporting landscape mode.

## Preview
<img src="/previews/screenshot_1.png" width="33%" /> <img src="/previews/screenshot_2.png" width="33%" /> 
<img src="/previews/screenshot_6.png" width="33%"/> <img src="/previews/screenshot_3.png" width="33%"/>
<img src="/previews/screenshot_4.png" width="33%"/> <img src="/previews/screenshot_5.png" width="33%"/>                                        

## Open API
Newsster uses the NewsApi for constructing RESTful API. Obtain your free API_KEY [NewsApi](https://newsapi.org/register) and paste it to the Constants file to try the app.

## License

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2020 Ersiver

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
