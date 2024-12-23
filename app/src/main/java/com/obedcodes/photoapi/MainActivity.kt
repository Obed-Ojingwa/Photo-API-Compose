package com.obedcodes.photoapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.obedcodes.photoapi.data.Recipe
import com.obedcodes.photoapi.data.RecipeResponse
import com.obedcodes.photoapi.network.NetworkManager
import com.obedcodes.photoapi.ui.theme.PhotoAPITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoAPITheme {
                RecipeScreen()
            }
        }
    }
}

// https://jsonplaceholder.typicode.com/photos
// https://dummyjson.com/recipes
@Composable
fun RecipeScreen(){



    val apiUrl = "https://dummyjson.com/recipes"
    val networkManager = NetworkManager()
    var recipeList by remember { mutableStateOf<List<Recipe>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch data asynchronously
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedTodos = networkManager.fetchRecipe(apiUrl)
                if (fetchedTodos != null) {
                    recipeList = fetchedTodos
                } else {
                    errorMessage = "Failed to fetch data. Please check your connection."
                }
            } catch (e: Exception) {
                errorMessage = "An unexpected error occurred: ${e.message}"
            }
        }
    }

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all=18.dp)
    ) {
        if (recipeList != null) {



            Column {

                Text(
                    text = "Featured Recipe",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipeList!!.take(5)) { recipe ->
                        FeaturedRecipeItem(recipe)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "All Recipes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )



                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight()
                ){
                    items(recipeList!!) { recipe ->
                        RecipeItem(recipe)
                    }
                }

            }
        } else if (errorMessage != null) {
            // Display the error message
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Show a loading spinner while data is being fetched
            Box(modifier = Modifier.align(Alignment.Center)) {
                CircularProgressIndicator()
            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedRecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(150.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            GlideImage(
                model = recipe.image,
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}





@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row {
            GlideImage(
                model = recipe.image,
                contentDescription = "Loaded from API",
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = recipe.cuisine,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}




