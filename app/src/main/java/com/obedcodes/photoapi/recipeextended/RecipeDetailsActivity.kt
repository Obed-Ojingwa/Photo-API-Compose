package com.obedcodes.photoapi.recipeextended



import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.obedcodes.photoapi.data.Recipe


class RecipeDetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the Recipe object from the intent
        val recipe = intent.getSerializableExtra("recipe") as? Recipe

        setContent {
            MaterialTheme {
                recipe?.let {
                    RecipeDetailsScreen(it)
                }  ?:run{
                    Text("Recipe not found!")
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeDetailsScreen(recipe: Recipe) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        GlideImage(
            model = recipe.image,
            contentDescription = recipe.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,

        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = recipe.name, style = MaterialTheme.typography.titleLarge, color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Cuisine: ${recipe.cuisine}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        when(recipe.difficulty){
            "Easy" ->  Text(text = "Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodyMedium, color = Color.Blue)
            "Medium" -> Text(text = "Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodyMedium, color = Color.Green)
            "Hard" -> Text(text = "Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ingredients:", style = MaterialTheme.typography.titleSmall)
        recipe.ingredients.forEach { ingredient ->
            Text("- $ingredient", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Instructions:", style = MaterialTheme.typography.titleSmall)
        recipe.instructions.forEachIndexed { index, instruction ->
            Text("${index + 1}. $instruction", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
