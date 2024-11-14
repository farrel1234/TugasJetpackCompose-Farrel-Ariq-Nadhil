// Navbar.kt
package com.example.myapplicationfarrel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

sealed class BottomNavItem(val title: String, val icon: @Composable () -> Unit, val screenRoute: String) {
    object Home : BottomNavItem("Beranda", { Icon(Icons.Default.Home, contentDescription = "Beranda") }, "beranda")
    object Search : BottomNavItem("Pencarian", { Icon(Icons.Default.Search, contentDescription = "Pencarian") }, "pencarian")
    object Favorites : BottomNavItem("Favorit", { Icon(Icons.Default.Favorite, contentDescription = "Favorit") }, "favorit")
    object Profile : BottomNavItem("Profile", { Icon(Icons.Default.Person, contentDescription = "Profile") }, "profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { item.icon() },
                label = { Text(item.title, color = Color.Black) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}
