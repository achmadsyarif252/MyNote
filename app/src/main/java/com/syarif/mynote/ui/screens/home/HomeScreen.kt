package com.syarif.mynote.ui.screens.home

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.syarif.mynote.ui.screens.login.AuthViewModel
import com.syarif.mynote.ui.screens.navigation.Screen
import com.syarif.mynote.ui.theme.BlueMain
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val showLogoutDialog = remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    var showHomeScreen by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        viewModel.getNotes()
    }

    LaunchedEffect(authState.logoutState) {
        if (authState.logoutState?.isSuccess == true) {
            showHomeScreen = false
            authViewModel.resetLogoutState()
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = true }
            }
        }
    }


    if (showHomeScreen) {
        Scaffold(
            topBar = {
                TopAppBar(
                    actions = {
                        IconButton(onClick = {
                            showLogoutDialog.value = true
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Exit"
                            )
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = "MY NOTE",
                                fontSize = 18.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "IconUser"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "${authState.userName}")
                            }
                        }
                    },
                )
                if (showLogoutDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog.value = false },
                        title = { Text(text = "Logout Confirmation") },
                        text = { Text(text = "Are you sure you want to logout?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    authViewModel.logout()
                                    showLogoutDialog.value = false
                                }
                            ) {
                                Text(text = "Logout", color = Color(0xFFF44336))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showLogoutDialog.value = false }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddNoteScreen.createRoute())
                    },
                    contentColor = Color.White,
                    containerColor = BlueMain,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        ) { padding ->
            if (uiState.isLoading)
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
            else if (uiState.message.isNotEmpty())
                Snackbar(
                    action = {
                        TextButton(onClick = { viewModel.getNotes() }) {
                            Text("Retry")
                        }
                    }
                ) { Text(uiState.message) }
            else
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                ) {

                    Text(
                        text = "Note List", modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                    )
                    DateRangePicker(onResetFilter = {
                        viewModel.clearFilter()
                    }, onDateRangeSelected = { a, b ->
                        viewModel.filterAndSortNotes(a, b)

                    }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        items(uiState.notes.size) {
                            Card(
                                onClick = {
                                    navController.currentBackStackEntry?.let { backStackEntry ->
                                        val bundle = Bundle().apply {
                                            putParcelable("note", uiState.notes[it])
                                        }
                                        val destination = backStackEntry.destination
                                        navController.navigate(destination.id, bundle)
                                    }
                                    navController.navigate(Screen.DetailNoteScreen.route)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .heightIn(min = 100.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically // Ensuring vertical alignment
                                    ) {
                                        Text(
                                            text = uiState.notes[it].title ?: "",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(0.8f)
                                        )
                                        Text(
                                            text = uiState.notes[it].createdAt ?: "",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(0.2f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = uiState.notes[it].note ?: "",
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                        }
                    }
                }
        }
    }
}


@Composable
fun DateRangePicker(
    onDateRangeSelected: (startDate: String, endDate: String) -> Unit,
    modifier: Modifier = Modifier,
    onResetFilter: () -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var isStartDatePicked by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            startDate = dateFormatter.format(calendar.time)
            isStartDatePicked = true
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            endDate = dateFormatter.format(calendar.time)
            onDateRangeSelected(startDate, endDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (isStartDatePicked) {
        endDatePickerDialog.show()
        isStartDatePicked = false
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                startDatePickerDialog.show()
            }
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            IconButton(onClick = {
                onResetFilter()
                startDate = ""
                endDate = ""
            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Reset Filter")
            }
        } else {
            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendar Icon")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (startDate.isNotEmpty() && endDate.isNotEmpty()) "$startDate - $endDate" else "Start Date - End Date")
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    val ctx = LocalContext.current
    val controller = NavHostController(ctx)
    HomeScreen(controller)
}