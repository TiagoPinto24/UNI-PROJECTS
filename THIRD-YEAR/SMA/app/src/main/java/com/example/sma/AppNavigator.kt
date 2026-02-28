package com.example.sma

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.sma.ui.theme.AppTheme
import com.example.sma.ui.StartMediaScreen
import com.example.sma.ui.StartAccountScreen
import com.example.sma.ui.StartLoginScreen
import com.example.sma.ui.StartMenuScreen
import com.example.sma.ui.StartSignInScreen

//Destinations that are not part of the Navigation rail
enum class NoRailScreens {
    Login,
    SignIn,
    MovieDetail
}

//Screens that are part of the Navigation rail
enum class RailScreens(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String
) {
    Menu("Menu", Icons.Filled.Menu, "Menu", "Menu Tab"),
    Account("Account", Icons.Filled.AccountCircle, "Account", "Account Tab")
}

//Generates the arrow to go back in the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentRailScreen = RailScreens.entries.firstOrNull { it.route == currentRoute }
    var showRail by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // if the screen we are currently have a backtrack
            if (currentRailScreen == null) {
                AppTopBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = RailScreens.Menu.route,
                modifier = Modifier.fillMaxSize()
            ) {
                //Routes
                composable(NoRailScreens.Login.name) {
                    AppTheme {
                        StartLoginScreen(
                            onClickSignIn = { navController.navigate(NoRailScreens.SignIn.name) },
                            onClickLogin = { navController.navigate(RailScreens.Menu.route) }
                        )
                    }
                }

                composable(NoRailScreens.SignIn.name) {
                    AppTheme { StartSignInScreen() }
                }

                composable(RailScreens.Menu.route) {
                    AppTheme {
                        StartMenuScreen(onItemClick = { multimedia ->
                            navController.navigate("${NoRailScreens.MovieDetail.name}/${multimedia.index}")
                        })

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 60.dp, start = 10.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            FilledIconButton(onClick = { showRail = true }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Open Navigation")
                            }
                        }
                    }
                }

                composable(RailScreens.Account.route) {
                    AppTheme {
                        StartAccountScreen(onClickSwitchAcc = {
                            navController.navigate(NoRailScreens.Login.name)
                        })

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 60.dp, start = 10.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            FilledIconButton(onClick = { showRail = true }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Open Navigation")
                            }
                        }
                    }
                }

                composable(
                    route = "${NoRailScreens.MovieDetail.name}/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.IntType
                        }
                    )
                ) { entry ->
                    val id = entry.arguments?.getInt("id") ?: return@composable
                    AppTheme {
                        StartMediaScreen(id = id)
                    }
                }
            }

            //Navigation rail
            if (showRail && currentRailScreen != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(72.dp),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        IconButton(onClick = { showRail = false }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close Navigation")
                        }

                        RailScreens.entries.forEach { destination ->
                            NavigationRailItem(
                                selected = destination.route == currentRoute,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    showRail = false
                                },
                                icon = {
                                    Icon(destination.icon, contentDescription = destination.contentDescription)
                                },
                                label = { Text(destination.label) }
                            )
                        }
                    }
                }
            }
        }
    }
}