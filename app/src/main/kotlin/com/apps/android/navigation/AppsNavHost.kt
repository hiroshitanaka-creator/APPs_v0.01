package com.apps.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apps.android.feature.auth.AuthScreen
import com.apps.android.feature.feed.FeedScreen
import com.apps.android.feature.post.PostCreateScreen
import com.apps.android.feature.profile.ProfileScreen
import com.apps.android.feature.search.SearchScreen

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Feed : Screen("feed")
    data object Search : Screen("search")
    data object PostCreate : Screen("post_create")
    data object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
}

@Composable
fun AppsNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = listOf(Screen.Feed.route, Screen.Search.route)
    val showBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                        label = { Text("ホーム") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Feed.route } == true,
                        onClick = {
                            navController.navigate(Screen.Feed.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = "検索") },
                        label = { Text("検索") },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Search.route } == true,
                        onClick = {
                            navController.navigate(Screen.Search.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = "投稿") },
                        label = { Text("投稿") },
                        selected = false,
                        onClick = { navController.navigate(Screen.PostCreate.route) },
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "プロフィール") },
                        label = { Text("マイページ") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Profile.createRoute("me")) },
                    )
                }
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.Feed.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.Feed.route) {
                FeedScreen(
                    onPostClick = { postId -> /* TODO: navigate to post detail */ },
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onPostClick = { postId -> /* TODO: navigate to post detail */ },
                )
            }
            composable(Screen.PostCreate.route) {
                PostCreateScreen(
                    onBack = { navController.popBackStack() },
                    onPostCreated = { navController.popBackStack() },
                )
            }
            composable(Screen.Profile.route) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ProfileScreen(
                    userId = userId,
                    onFollowClick = {},
                )
            }
        }
    }
}
