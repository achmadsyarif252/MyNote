package com.syarif.mynote.ui.screens.detail_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.ui.screens.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteScreen(
    navController: NavHostController,
    viewModel: DetailNoteViewModel = hiltViewModel(),
    noteItem: ListNoteItem
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val showDetailContent by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()
    var note = uiState.note


    LaunchedEffect(key1 = noteItem.id) {
        viewModel.getDetailNote(noteItem.id!!) {
            note = it
        }
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.message,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
            viewModel.resetMsg()
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "MY NOTE") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (showDetailContent && uiState.note != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = uiState.note?.title ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Text(
                        text = uiState.note?.createdAt ?: "",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.note?.note ?: "",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            ),
                            onClick = {
                                val noteItemJson = Gson().toJson(note)
                                navController.navigate(Screen.AddNoteScreen.createRoute(noteItemJson))
                            }
                        ) {
                            Text(text = "EDIT", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
                                contentColor = Color.White
                            ),
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Text(text = "DELETE", fontWeight = FontWeight.Bold)
                        }
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text(text = "Delete Note?") },
                            text = { Text(text = "This note will be permanently removed from your account.") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        note?.id?.let { id ->
                                            viewModel.delete(id) {
                                                if (it) navController.popBackStack()
                                            }
                                        }
                                        showDeleteDialog = false
                                    }
                                ) {
                                    Text(text = "DELETE", color = Color(0xFFF44336))
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDeleteDialog = false }
                                ) {
                                    Text(text = "CANCEL")
                                }
                            }
                        )
                    }

                }
            }
        }
    }
}


