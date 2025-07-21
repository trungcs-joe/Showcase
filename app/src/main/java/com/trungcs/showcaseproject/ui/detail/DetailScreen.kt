package com.trungcs.showcaseproject.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.model.Note
import com.trungcs.showcaseproject.ui.theme.NoteTheme
import com.trungcs.showcaseproject.R
import com.trungcs.showcaseproject.util.OnButtonClick

@Composable
fun DetailScreen(
    onBackToList: OnButtonClick,
    onEmptyTitleError: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    Column {
        DetailAppBar(
            onSaveClick = viewModel::onSave,
            onBackClick = onBackToList,
            isSavable = uiState.isSaveAble()
        )

        when (uiState) {
            is DetailUiState.Loading -> CircularProgressIndicator()
            is DetailUiState.Error -> TODO()
            is DetailUiState.Success -> NoteContent(
                state = uiState as DetailUiState.Success,
                onEmptyTitleError = {
                    onEmptyTitleError()
                    viewModel.clearEmptyTitleError()
                },
                onTitleChange = viewModel::onTitleChange,
                onContentChange = viewModel::onContentChange,
                onDone = onBackToList
            )
        }
    }
}

@Composable
fun NoteContent(
    state: DetailUiState.Success,
    onTitleChange: ((String) -> Unit)? = null,
    onContentChange: ((String) -> Unit)? = null,
    onEmptyTitleError: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
) {
    if (state.isEmptyTitleError) {
        onEmptyTitleError?.invoke()
    }

    if (state.isDone) {
        onDone?.invoke()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            label = { Text(text = stringResource(id = R.string.enter_title)) },
            value = state.newTitle,
            onValueChange = { onTitleChange?.invoke(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleLarge.copy(),
        )
        TextField(
            value = state.newContent,
            onValueChange = { onContentChange?.invoke(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            label = { Text(text = stringResource(id = R.string.enter_note)) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(),
        )

    }
}

@Composable
fun DetailAppBar(onSaveClick: OnButtonClick, onBackClick: OnButtonClick, isSavable: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(enabled = isSavable, onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.Close,
                tint = Color.White,
                contentDescription = "backIcon"
            )
        }

        Text(
            maxLines = 1,
            text = stringResource(id = R.string.note_detail),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        TextButton(enabled = true, onClick = onSaveClick) {
            Text(text = stringResource(id = R.string.save), color = Color.White)
        }
    }
}

@Preview(heightDp = 60)
@Composable
fun AppbarPreview() {
    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        NoteTheme {
            DetailAppBar(
                onSaveClick = {},
                onBackClick = {},
                isSavable = true

            )
        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        NoteTheme {
            NoteContent(
                state = DetailUiState.Success(
                    note = Note(title = "", content = ""),
                    newTitle = "Connected to process 6758",
                    newContent = "Connected to process 6758 on device 'Nexus_5X_API_29 [emulator-5554]",
                ),
            )
        }
    }
}
