## Recipe App

This project demonstrates how to fetch and display data from a JSON API in an Android app using Kotlin, Jetpack Compose, and Glide for image loading. The app displays a list of recipes fetched from the API, including recipe names, cuisines, and images.

## Features

Fetches data from a JSON API: https://dummyjson.com/recipes.

Displays recipes in a LazyColumn using Jetpack Compose.

Uses Glide to load and display recipe images.

Handles network errors gracefully and provides placeholders for loading and error states.

Demonstrates modular Kotlin code with a clean structure.

Libraries and Tools Used

Jetpack Compose: Modern UI toolkit for building declarative UIs in Android.

Glide: An image loading library for Android.

OkHttp: Handles network requests.

Gson: Parses JSON responses into Kotlin data classes.

Kotlin Coroutines: For asynchronous operations.

## Getting Started

Prerequisites

* Android Studio (Ladybug).

* Kotlin support enabled.

* Internet connection for API requests.

* API

The app fetches data from the API endpoint:

https://dummyjson.com/recipes

The API returns a list of recipes with attributes like name, cuisine, image, ingredients, instructions, etc.
