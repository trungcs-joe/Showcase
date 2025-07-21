package com.trungcs.showcaseproject.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.common.result.Result
import com.note.data.repo.NoteRepository
import com.note.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiStateFlow: StateFlow<ListUiState> = _uiStateFlow

    init {
        viewModelScope.launch {
            noteRepository.getAllNoteEntities().mapLatest { result ->
                when (result) {
                    is Result.Error -> ListUiState.Error
                    is Result.Loading -> ListUiState.Loading
                    is Result.Success -> ListUiState.Success(result.data)
                }
            }.collectLatest {
                _uiStateFlow.value = it
            }
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            noteRepository.deleteNotes(listOf(noteId))
        }
    }
}

sealed interface ListUiState {
    data class Success(val listNotes: List<Note>) : ListUiState
    data object Error : ListUiState
    data object Loading : ListUiState
}