package com.trungcs.showcaseproject.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.trungcs.showcaseproject.ui.detail.route.detailScreen
import com.trungcs.showcaseproject.ui.detail.route.navigateToDetail
import com.trungcs.showcaseproject.ui.list.route.LIST_ROUTE
import com.trungcs.showcaseproject.ui.list.route.listScreen
import com.trungcs.showcaseproject.ui.theme.NoteTheme
import com.trungcs.showcaseproject.R
import kotlinx.coroutines.launch


@Composable
fun NoteApp(
    navController: NavHostController = rememberNavController()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                Snackbar(
                    it,
                    containerColor = Color.Yellow,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
            }
        },
    ) { innerPadding ->
        val errorText = stringResource(id = R.string.enter_title_message)

        NavHost(
            navController = navController,
            startDestination = LIST_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            listScreen { navController.navigateToDetail(it) }
            detailScreen(
                onBackClick = { navController.popBackStack() },
                onEmptyTitleError = {
                    coroutineScope.launch { snackBarHostState.showSnackbar(errorText) }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteTheme(darkTheme = true) {
        NoteApp()
    }
}
