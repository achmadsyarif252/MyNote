package com.syarif.mynote.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.ui.screens.addnote.AddNoteScreen
import com.syarif.mynote.ui.screens.detail_note.DetailNoteScreen
import com.syarif.mynote.ui.screens.home.HomeScreen
import com.syarif.mynote.ui.screens.login.AuthViewModel
import com.syarif.mynote.ui.screens.login.LoginScreen
import com.syarif.mynote.ui.screens.register.RegisterScreen
import kotlinx.coroutines.runBlocking

@Composable
fun SetUpNavGraph(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val initialDestination = getInitialDestination(viewModel) // Function to determine destination

    NavHost(
        navController = navController,
        startDestination = initialDestination
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.AddNoteScreen.route + "?${Screen.AddNoteScreen.NOTE_ITEM_KEY}={${Screen.AddNoteScreen.NOTE_ITEM_KEY}}",
            arguments = listOf(
                navArgument(Screen.AddNoteScreen.NOTE_ITEM_KEY) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val noteItemJson =
                backStackEntry.arguments?.getString(Screen.AddNoteScreen.NOTE_ITEM_KEY)
            val noteItem = Gson().fromJson(noteItemJson, ListNoteItem::class.java)

            AddNoteScreen(navController = navController, noteItem = noteItem)
        }

        composable(Screen.DetailNoteScreen.route) { backStackEntry ->
            val note =
                navController.previousBackStackEntry?.arguments?.getParcelable<ListNoteItem>("note")
            if (note != null) {
                DetailNoteScreen(navController = navController, noteItem = note)
            }
        }
    }
}


@Composable
private fun getInitialDestination(viewModel: AuthViewModel): String {
    val userToken = runBlocking { viewModel.readUserToken() }
    return if (userToken != null) Screen.HomeScreen.route else Screen.LoginScreen.route
}

