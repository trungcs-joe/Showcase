package com.trungcs.showcaseproject.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.common.result.Result
import com.note.data.repo.NoteRepository
import com.note.model.Note
import com.trungcs.showcaseproject.ui.detail.route.NOTE_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

const val CREATE_NEW_NOTE_ID = -1

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepo: NoteRepository,
) : ViewModel() {

    private val noteId: StateFlow<Int> =
        savedStateHandle.getStateFlow(NOTE_ID_ARG, CREATE_NEW_NOTE_ID)

    private val _uiStateFlow = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiStateFlow: StateFlow<DetailUiState> = _uiStateFlow

    init {
        viewModelScope.launch {
            noteId.mapLatest {
                if (it == -1) Result.Success(null) else noteRepo.getNoteById(it)
            }.map { result ->
                when (result) {
                    is Result.Error -> DetailUiState.Error
                    is Result.Loading -> DetailUiState.Loading
                    is Result.Success -> {
                        result.data?.let {
                            DetailUiState.Success(
                                note = it,
                                newTitle = it.title,
                                newContent = it.content,
                            )
                        } ?: DetailUiState.Success()
                    }
                }
            }.collect {
                _uiStateFlow.value = it
            }
        }
    }

    fun clearEmptyTitleError() {
        val currentState = _uiStateFlow.value
        if (currentState is DetailUiState.Success) {
            _uiStateFlow.value = currentState.copy(isEmptyTitleError = false)
        }
    }

    fun onTitleChange(title: String) {
        val currentState = _uiStateFlow.value
        if (currentState is DetailUiState.Success) {
            _uiStateFlow.value = currentState.copy(newTitle = title)
        }
    }

    fun onContentChange(content: String) {
        val currentState = _uiStateFlow.value
        if (currentState is DetailUiState.Success) {
            _uiStateFlow.value = currentState.copy(newContent = content)
        }
    }

    fun onSave() {
        viewModelScope.launch {
            val currentState = _uiStateFlow.value
            if (currentState !is DetailUiState.Success) {
                return@launch
            }

            if (currentState.isTitleEmpty()) {
                _uiStateFlow.value = currentState.copy(isEmptyTitleError = true)
                return@launch
            }

            val updatingNote = Note(
                id = currentState.note?.id ?: 0,
                content = currentState.newContent,
                title = currentState.newTitle,
                lastModified = Date()

            )

            val result = noteRepo.upsertNote(updatingNote)
            if (result is Result.Success) {
                _uiStateFlow.value = currentState.copy(
                    isDone = true,
                )
            }
        }
    }
}

sealed interface DetailUiState {
    data class Success(
        // node == null means creating new one
        // note != null means updating an existing note.
        val note: Note? = null,
        val newTitle: String = "",
        val newContent: String = "",
        val isEmptyTitleError: Boolean = false,
        val isDone: Boolean = false,

        ) : DetailUiState {

        fun isTitleEmpty() = newTitle.isEmpty()
    }

    data object Error : DetailUiState

    data object Loading : DetailUiState

    fun isSaveAble() = this is Success && !isTitleEmpty()
}


