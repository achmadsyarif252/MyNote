package com.syarif.mynote.ui.screens.navigation

import com.syarif.mynote.core.domain.model.ListNoteItem

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("login")
    data object RegisterScreen : Screen("register")
    data object HomeScreen : Screen("home")
    data object AddNoteScreen : Screen("add_note") {
        const val NOTE_ITEM_KEY = "noteItem"
        fun createRoute(noteItemJson: String? = null): String {
            return if (noteItemJson != null) {
                "add_note?$NOTE_ITEM_KEY=$noteItemJson"
            } else {
                "add_note"
            }
        }
    }

    data object DetailNoteScreen : Screen("detail_note/{noteItem}")
}