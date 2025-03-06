package com.obedcodes.photoapi.recipeextended



import android.media.RouteListingPreference
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            .verticalScroll(rememberScrollState())
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
        Text(text = recipe.name, style = MaterialTheme.typography.displayMedium, color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Cuisine: ${recipe.cuisine}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Calories contained: ${recipe.caloriesPerServing}",
                style = MaterialTheme.typography.bodyLarge, color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Expected Minutes: ${recipe.prepTimeMinutes}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Expected Cook Time: ${recipe.cookTimeMinutes}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ratings: ${recipe.rating}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Review Count: ${recipe.reviewCount}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val uniVal = Text(
            text = "Difficulty: ${recipe.difficulty}",
            style = MaterialTheme.typography.displaySmall
        )
        when (recipe.difficulty) {
            "Easy" -> uniVal
            "Medium" -> uniVal
            "Hard" -> uniVal
        }

        Spacer(modifier = Modifier.height(16.dp))


            Text(text = "Ingredients:", style = MaterialTheme.typography.displaySmall)
            recipe.ingredients.forEach { ingredient ->
                Text("- $ingredient", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Instructions:", style = MaterialTheme.typography.displaySmall)
            recipe.instructions.forEachIndexed { index, instruction ->
                Text("${index + 1}. $instruction", style = MaterialTheme.typography.labelLarge)
            }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Meal Type:", style = MaterialTheme.typography.displaySmall)
        recipe.mealType.forEach{mealType ->
            Text(text = "- $mealType", MaterialTheme.typography.labelLarge)
        }
    }
}
