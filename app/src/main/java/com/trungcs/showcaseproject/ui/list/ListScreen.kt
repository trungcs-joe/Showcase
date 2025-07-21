package com.trungcs.showcaseproject.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.model.Note
import com.trungcs.showcaseproject.ui.component.LoadingWheel
import com.trungcs.showcaseproject.ui.component.RandomColorCard
import com.trungcs.showcaseproject.ui.detail.CREATE_NEW_NOTE_ID
import com.trungcs.showcaseproject.ui.theme.NoteTheme
import com.trungcs.showcaseproject.R
import com.trungcs.showcaseproject.util.OnButtonClick

@Composable
fun ListNoteScreen(
    onSelectNoteClick: (Int) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    // showDeleteDialog stores a pair of values.
    // first stores a flag to open/close the dialog
    // second stores the id of the note
    var showDeleteDialog by remember { mutableStateOf(false to 0) }
    if (showDeleteDialog.first) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false to 0 },
            title = { Text(stringResource(id = R.string.confirm_delete)) },
            text = { Text(stringResource(id = R.string.are_you_sure_to_delete_note)) },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteNote(showDeleteDialog.second)
                    showDeleteDialog = false to 0
                }) {
                    Text(stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false to 0 }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Column {
        ListAppBar()
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is ListUiState.Loading -> LoadingWheel(
                    "Loading",
                    modifier = Modifier.align(Alignment.Center)
                )

                is ListUiState.Error -> TODO()
                is ListUiState.Success -> ListNoteBody(
                    onCreateNewNodeClick = { onSelectNoteClick(CREATE_NEW_NOTE_ID) },
                    onSelectNoteClick = onSelectNoteClick,
                    onDeleteNoteClick = { showDeleteDialog = true to it },
                    notes = (uiState as ListUiState.Success).listNotes
                )
            }
        }
    }
}

@Composable
fun ListAppBar() {
    Column(
        modifier = Modifier.padding(horizontal = 32.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(id = R.string.list_note_title),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
        )

    }
}

@Composable
fun ListNoteBody(
    onSelectNoteClick: (Int) -> Unit,
    onDeleteNoteClick: (Int) -> Unit,
    onCreateNewNodeClick: OnButtonClick,
    notes: List<Note>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(32.dp),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(notes) {
                NoteItem(
                    note = it,
                    onSelectNoteClick = onSelectNoteClick,
                    onDeleteNoteClick = onDeleteNoteClick,
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(64.dp),
            onClick = onCreateNewNodeClick,
        ) {
            Icon(Icons.Filled.Add, "Add", tint = Color.White)
        }
    }

}

@Composable
fun NoteItem(note: Note, onSelectNoteClick: (Int) -> Unit, onDeleteNoteClick: (Int) -> Unit) {
    RandomColorCard(onCardClick = { onSelectNoteClick.invoke(note.id) }) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 4
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                )
            }

            Icon(
                modifier = Modifier
                    .width(28.dp)
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onDeleteNoteClick(note.id) },
                imageVector = Icons.Default.Delete,
                tint = Color.Black.copy(alpha = 0.5f),
                contentDescription = "deleteIcon"
            )
        }
    }
}


@Preview(heightDp = 128)
@Composable
fun AppBarPreview() {
    NoteTheme {
        Surface(
            color = Color.Black,
            modifier = Modifier.fillMaxSize()
        ) {
            ListAppBar()
        }
    }
}


@Preview
@Composable
fun ListScreenPreview() {
    NoteTheme {
        Surface(
            color = Color.Black,
            modifier = Modifier.fillMaxSize()
        ) {
            ListNoteBody(
                onSelectNoteClick = {},
                onCreateNewNodeClick = {},
                onDeleteNoteClick = {},
                notes = listOf(
                    Note(
                        title = "TitleTitleTitleTitleTitle",
                        content = "To set a fixed number of columns, you can use"
                    ),
                    Note(
                        title = "This is the new note",
                        content = "This divides the available width by the number of columns (or rows for a horizontal grid), and has each item take up that width (or height for a horizontal grid):"
                    ),
                    Note(
                        title = "Title 1",
                        content = "Please note that this padding is applied to the content, not to the LazyColumn itself. In the example above, the first item will add 8.dp padding to it’s top, the last item will add 8.dp to its bottom, and all items will have 16.dp padding on the left and the right."
                    ),
                    Note(
                        title = "Title 1",
                        content = "To set a fixed number of columns, you can use"
                    ),
                    Note(
                        title = "Title 1",
                        content = "To set a fixed number of columns, you can use"
                    ),
                    Note(
                        title = "Title 1",
                        content = "To set a fixed number of columns, you can use"
                    ),
                )
            )
        }

    }
}
