package com.obedcodes.photoapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.obedcodes.photoapi.data.Recipe
import com.obedcodes.photoapi.data.RecipeResponse
import com.obedcodes.photoapi.network.NetworkManager
import com.obedcodes.photoapi.recipeextended.RecipeDetailsActivity
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
    ///    TOdo completed                1
    // I Created a new activity (RecipeDetailsActivity) that will display the details of a clicked recipe.

    // https://jsonplaceholder.typicode.com/photos
// https://dummyjson.com/recipes
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RecipeScreen() {


        val apiUrl = "https://dummyjson.com/recipes"
        val networkManager = NetworkManager()
        var recipeList by remember { mutableStateOf<List<Recipe>?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var isRefreshing by remember { mutableStateOf(false) }

        val context = LocalContext.current

        /* // PullRefresh state
    val pullRefreshState = rememberPullToRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            refreshRecipes(apiUrl, networkManager, { recipes ->
                recipeList = recipes
                errorMessage = null
                isRefreshing = false
            }, { error ->
                errorMessage = error
                isRefreshing = false
            })
        }
    )*/

        // Fetch data asynchronously
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    isRefreshing = true
                    val fetchedRecipe = networkManager.fetchRecipe(apiUrl)
                    if (fetchedRecipe != null) {
                        recipeList = fetchedRecipe
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
                .padding(all = 18.dp)
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
                            FeaturedRecipeItem(recipe){clickedItem ->
                                val intent = Intent(context, RecipeDetailsActivity::class.java)
                                intent.putExtra("recipe", clickedItem)
                                startActivity(intent)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "All Recipes",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )



                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(recipeList!!) { recipe ->
                            // Navigating to RecipeDetailsActivity
                            RecipeItem(recipe){clickedRecipe ->
                                // val context = LocalContext.current has been defined above because of the error I encounterd doing it here
                                val intent = Intent(context, RecipeDetailsActivity::class.java)
                                intent.putExtra("recipe", clickedRecipe)
                                startActivity(intent)
                            }
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
    fun FeaturedRecipeItem(recipe: Recipe, onClick: (Recipe) -> Unit) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
                .clickable { onClick(recipe) },
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
//       TOdo completed                   2
  //  I Updated the RecipeItem composable to navigate to RecipeDetailsActivity when clicked
    // using the .clickable modifier and I added a onClick as an argument to the parameter

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun RecipeItem(recipe: Recipe, onClick : (Recipe) ->Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
                .clickable { onClick(recipe)  },
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

    private fun refreshRecipes(
        apiUrl: String,
        networkManager: NetworkManager,
        onSuccess: (List<Recipe>) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedRecipes = networkManager.fetchRecipe(apiUrl)
                if (fetchedRecipes != null) {
                    onSuccess(fetchedRecipes)
                } else {
                    onError("Failed to fetch data. Please check your connection.")
                }
            } catch (e: Exception) {
                onError("An unexpected error occurred: ${e.message}")
            }
        }
    }


}