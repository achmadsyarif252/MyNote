package com.syarif.mynote.ui.screens.addnote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.syarif.mynote.core.domain.model.ListNoteItem
import com.syarif.mynote.ui.theme.BlueMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteViewModel = hiltViewModel(),
    noteItem: ListNoteItem? = null
) {
    var title by rememberSaveable { mutableStateOf(noteItem?.title ?: "") }
    var noteContent by rememberSaveable { mutableStateOf(noteItem?.note ?: "") }
    var errorTitle by remember { mutableStateOf(false) }
    var errorContent by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(uiState) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar(
                message = uiState.message,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            viewModel.resetMessage()
            viewModel.resetSuccess()
        } else if (uiState.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.message,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
            viewModel.resetMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "MY NOTE") })
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = {
                        title = it
                        errorTitle = it.trim().isEmpty()
                    },
                    label = {
                        Text(text = "Title")
                    },
                    isError = errorTitle,
                    singleLine = true
                )
                if (errorTitle) {
                    Text(
                        text = "Title cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                TextField(
                    value = noteContent,
                    onValueChange = {
                        noteContent = it
                        errorContent = it.trim().isEmpty()
                    },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    isError = errorContent,
                    maxLines = Int.MAX_VALUE,
                )
                if (errorContent) {
                    Text(
                        text = "Content cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.weight(0.4f))
                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val isTitleEmpty = title.trim().isEmpty()
                        val isContentEmpty = noteContent.trim().isEmpty()

                        errorTitle = isTitleEmpty
                        errorContent = isContentEmpty

                        if (!isTitleEmpty && !isContentEmpty) {
                            if (noteItem != null) {
                                noteItem.id?.let { it1 ->
                                    viewModel.updateNote(
                                        it1,
                                        title,
                                        noteContent
                                    )
                                }
                            } else {
                                viewModel.insert(title, noteContent)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueMain,
                        contentColor = Color.White,
                    )
                ) {
                    Text(text = "Save", fontSize = 16.sp)
                }
            }
        }
    }
}


@Preview
@Composable
private fun AddNoteScreenPreview() {
    val ctx = LocalContext.current
    val controller = NavHostController(ctx)
    AddNoteScreen(navController = controller, noteItem = null)

}