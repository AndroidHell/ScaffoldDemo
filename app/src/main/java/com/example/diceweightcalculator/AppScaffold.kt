package com.example.diceweightcalculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.diceweightcalculator.screens.DonateScreen
import com.example.diceweightcalculator.screens.HomeScreen
import com.example.diceweightcalculator.screens.SessionsScreen
import com.example.diceweightcalculator.screens.SettingsScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.diceweightcalculator.screens.NewSessionScreen




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    var navigationSelectedItem by remember { mutableStateOf(0) }
    val navController = rememberNavController()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val currentScreenTitle = remember { mutableStateOf("") }
    val showBackArrow = remember { mutableStateOf(false) }

    var isFabPressed by remember { mutableStateOf(false) }
    val bottomBarOffset by animateFloatAsState(targetValue = if (isFabPressed) 75f else 0f)

    val currentScreenIcon = remember { mutableStateOf<ImageVector?>(null) } // Initialize with null

    LaunchedEffect(currentDestination?.route) {
        currentScreenTitle.value = when (currentDestination?.route) {
            Screens.Home.route -> {
                showBackArrow.value = false
                currentScreenIcon.value = Icons.Default.Home
                "Home"
            }
            Screens.Sessions.route -> {
                showBackArrow.value = true
                currentScreenIcon.value = Icons.Default.Edit
                "Sessions"
            }
            Screens.Settings.route -> {
                showBackArrow.value = true
                currentScreenIcon.value = Icons.Default.Settings
                "Settings"
            }
            Screens.Donate.route -> {
                showBackArrow.value = true
                currentScreenIcon.value = Icons.Default.Favorite
                "Donate"
            }
            Screens.NewSession.route -> {
                showBackArrow.value = true
                currentScreenIcon.value = Icons.Default.ArrowBack
                "New Session"
            }
            else -> {
                showBackArrow.value = false
                currentScreenIcon.value = Icons.Default.Home
                "Home"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = currentScreenTitle.value,
                icon = currentScreenIcon.value,
                onIconClick = {
                    if (currentScreenIcon.value ==Icons.Default.ArrowBack) {
                        isFabPressed = false
                        navController.popBackStack(Screens.Home.route, inclusive = false)
                    } else {
                        null
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            Box(
                modifier = Modifier
                    .offset(y = bottomBarOffset.dp)
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(300)
                    )
            ) {
                NavigationBar {
                    BottomNavigationItem().bottomNavigationItems().forEachIndexed {index,navigationItem ->
                        NavigationBarItem(
                            selected = index == navigationSelectedItem,
                            label = {
                                Text(navigationItem.label)
                            },
                            icon = {
                                Icon(
                                    navigationItem.icon,
                                    contentDescription = navigationItem.label
                                )
                            },
                            onClick = {
                                navigationSelectedItem = index
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentDestination?.route == Screens.Home.route,
                enter = scaleIn(animationSpec = tween(300)),
                exit = scaleOut(animationSpec = tween(300))
            ) {
                FloatingActionButton(
                    onClick = {
                        isFabPressed = !isFabPressed
                        if (isFabPressed) {
                            navController.navigate(Screens.NewSession.route)
                        }

                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)) {
            composable(Screens.Home.route) {
                HomeScreen(
                    navController
                )
            }
            composable(Screens.Sessions.route) {
                SessionsScreen(
                    navController
                )
            }
            composable(Screens.Settings.route) {
                SettingsScreen(
                    navController
                )
            }
            composable(Screens.Donate.route) {
                DonateScreen(
                    navController
                )
            }
            composable(Screens.NewSession.route) {
                NewSessionScreen(
                    navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String, icon: ImageVector? = null, onIconClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            icon?.let {
                IconButton(onClick = onIconClick) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }
    )
}