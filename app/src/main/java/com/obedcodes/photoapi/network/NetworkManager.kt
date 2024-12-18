package com.obedcodes.photoapi.network

import com.google.gson.Gson
import com.obedcodes.photoapi.data.Recipe
import com.obedcodes.photoapi.data.RecipeResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException


class NetworkManager {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchRecipe(apiUrl: String): List<Recipe>? {

        try {
            // Validate URL format
            if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
                throw IllegalArgumentException("Invalid URL: $apiUrl")
            }
            val request = Request.Builder()
                .url(apiUrl)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseBody ->


                     //   return gson.fromJson(responseBody, Array<Recipe>::class.java).toList()
                        // Deserialize the Top-Level RecipeResponse
                     val recipeRecipe = gson.fromJson(responseBody, RecipeResponse::class.java)
                        return recipeRecipe.recipes

                    }
                } else {
                    println("HTTP error: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: IOException) {
            // Handle network-related exceptions (e.g., no internet)
            println("Network error: ${e.message}")
        } catch (e: IllegalArgumentException) {
            // Handle invalid URL
            println("Invalid URL error: ${e.message}")
        } catch (e: Exception) {
            // Handle other unexpected exceptions
            println("Unexpected error: ${e.message}")
        }

        // Return null if something went wrong
        return null
    }
}
