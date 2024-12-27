package com.obedcodes.photoapi.data

import java.io.Serializable


data class RecipeResponse(
    val recipes: List<Recipe>
) : Serializable

data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val cuisine: String,
    val caloriesPerServing: Int,
    val tags: List<String>,
    val userId: Int,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val mealType: List<String>
) : Serializable

/*data class Recipe(
    val albumID : Int,
    val id:Int,
    val title : String,
    val url : String,
    val thumbnailUrl : String
)*/

/*
*
*   "albumId": 1,
    "id": 1,
    "title": "accusamus beatae ad facilis cum similique qui sunt",
    "url": "https://via.placeholder.com/600/92c952",
    "thumbnailUrl": "https://via.placeholder.com/150/92c952"*/

