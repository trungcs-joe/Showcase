package com.trungcs.showcaseproject.ui.detail.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.trungcs.showcaseproject.ui.detail.DetailScreen
import com.trungcs.showcaseproject.util.OnButtonClick

const val NOTE_ID_ARG = "noteId"
const val DETAIL_ROUTE_BASE = "detailed_note_route"
const val DETAIL_ROUTE = "$DETAIL_ROUTE_BASE?$NOTE_ID_ARG={$NOTE_ID_ARG}"

fun NavController.navigateToDetail(noteId: Int, navOptions: NavOptions? = null) {
    navigate("$DETAIL_ROUTE_BASE?$NOTE_ID_ARG=$noteId", navOptions)

}

fun NavGraphBuilder.detailScreen(
    onBackClick: OnButtonClick,
    onEmptyTitleError: () -> Unit,
) {
    composable(
        route = DETAIL_ROUTE,
        arguments = listOf(
            navArgument(NOTE_ID_ARG) {
                defaultValue = 0
                nullable = false
                type = NavType.IntType
            },
        ),
    ) {
        DetailScreen(
            onBackToList = onBackClick,
            onEmptyTitleError = onEmptyTitleError,
        )
    }
}
