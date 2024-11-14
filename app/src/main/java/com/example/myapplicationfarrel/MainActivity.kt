// MainActivity.kt
package com.example.myapplicationfarrel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationfarrel.ui.theme.MyApplicationFarrelTheme
import androidx.compose.foundation.shape.CircleShape


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationFarrelTheme {
                MovieApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieApp() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { index -> selectedItem = index }
            )
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> FilmMovie(modifier = Modifier.padding(innerPadding))
            1 -> PencarianScreen(modifier = Modifier.padding(innerPadding))
            2 -> FavoritScreen(modifier = Modifier.padding(innerPadding))
            3 -> ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

// Remaining functions like FilmMovie, PencarianScreen, FavoritScreen, ProfileScreen, WelcomeSection, and kartunonton stay unchanged.
@Composable
fun FilmMovie(modifier: Modifier = Modifier) {
    val movies = listOf(
        "The Shawshank Redemption",
        "The Godfather",
        "The Dark Knight",
        "Pulp Fiction",
        "Fight Club",
        "Inception",
        "The Matrix",
        "Forrest Gump"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            WelcomeSection()
        }

        items(movies) { movie ->
            kartunonton(movieTitle = movie)
        }
    }
}

@Composable
fun PencarianScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val movies = listOf(
        "The Shawshank Redemption",
        "The Godfather",
        "The Dark Knight",
        "Pulp Fiction",
        "Fight Club",
        "Inception",
        "The Matrix",
        "Forrest Gump"
    )

    val filteredMovies = movies.filter { movie ->
        movie.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Movies") },
            modifier = Modifier.fillMaxWidth()
        )

        if (filteredMovies.isEmpty()) {
            Text(
                text = "No movies found",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(filteredMovies) { movie ->
                    kartunonton(movieTitle = movie)
                }
            }
        }
    }
}
@Composable
fun FavoritScreen(modifier: Modifier = Modifier) {
    // State for managing the favorite movie list
    var favoriteMovies by remember { mutableStateOf(mutableListOf<String>()) }

    // List of movies to be added to favorites
    val allMovies = listOf(
        "The Shawshank Redemption",
        "The Godfather",
        "The Dark Knight",
        "Pulp Fiction",
        "Fight Club",
        "Inception",
        "The Matrix",
        "Forrest Gump"
    )

    // State to control dialog visibility
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title for the favorites page
        Text(
            text = "Favorit Saya",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the list of favorite movies
        if (favoriteMovies.isEmpty()) {
            Text(
                text = "Belum ada film favorit.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(favoriteMovies) { movie ->
                    KartunontonWithRemove(
                        movieTitle = movie,
                        onRemove = {
                            favoriteMovies.remove(movie)
                            dialogMessage = "Film '$movie' telah dihapus dari favorit."
                            showDialog = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section to add movies to favorites
        Text(
            text = "Tambah Film ke Favorit",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(allMovies) { movie ->
                KartunontonWithAdd(
                    movieTitle = movie,
                    onAdd = {
                        if (!favoriteMovies.contains(movie)) {
                            favoriteMovies.add(movie)
                            dialogMessage = "Film '$movie' telah ditambahkan ke favorit."
                            showDialog = true
                        }
                    }
                )
            }
        }
    }

    // Dialog for confirmation after adding/removing movies
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun KartunontonWithAdd(movieTitle: String, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movieTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Klik untuk menambahkan ke favorit",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = onAdd) {
                Text(text = "Tambah ke Favorit")
            }
        }
    }
}

@Composable
fun KartunontonWithRemove(movieTitle: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movieTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Klik untuk menghapus dari favorit",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = onRemove) {
                Text(text = "Hapus dari Favorit")
            }
        }
    }
}


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    // Data pengguna contoh
    val userName = "Farrel"
    val userEmail = "farrelariq2@gmail.com"
    val perguruantinggi = "Politeknik Negeri Batam"
    val jurusan = "Android Mobile Development & UIUX Design"
    val userProfilePic = "https://www.example.com/profilepic.jpg" // URL contoh untuk gambar profil

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar Profil (Anda dapat memuat gambar menggunakan Coil atau menggunakan gambar placeholder)
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = Color.Gray
        ) {
            // Anda dapat memuat gambar menggunakan Coil
            // Image(painter = rememberImagePainter(userProfilePic), contentDescription = "Gambar Profil")
        }

        Text(
            text = userName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = perguruantinggi,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = jurusan,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = userEmail,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol logout atau pengaturan profil lainnya
        Button(
            onClick = {
                // Implementasikan logika logout atau aksi lain
                // Misalnya, arahkan ke layar login atau hapus data sesi pengguna
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}


@Composable
fun WelcomeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ayo menonton film",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black  // Changed to black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Semua film disini adalah yang terbaik",
            fontSize = 16.sp,
            color = Color.Black,  // Changed to black
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun kartunonton(movieTitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movieTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black  // Changed to black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "klik untuk melihat lebih detail",
                fontSize = 14.sp,
                color = Color.Black,  // Changed to black
                fontWeight = FontWeight.Normal
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieAppPreview() {
    MyApplicationFarrelTheme {
        MovieApp()
    }
}