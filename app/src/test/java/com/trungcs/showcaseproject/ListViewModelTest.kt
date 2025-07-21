package com.trungcs.showcaseproject

import com.note.common.result.Result
import com.note.data.repo.NoteRepository
import com.note.model.Note
import com.trungcs.showcaseproject.repo.MockNoteRepository
import com.trungcs.showcaseproject.util.MainDispatcherRule
import com.trungcs.showcaseproject.ui.list.ListUiState
import com.trungcs.showcaseproject.ui.list.ListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val errorMockRepository: NoteRepository = mock()
    private val successMockRepository = MockNoteRepository()
    private lateinit var viewModel: ListViewModel

    @Test
    fun ListViewModel_test_init_state() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel = ListViewModel(successMockRepository)
            assert(viewModel.uiStateFlow.value is ListUiState.Loading)
        }
        collectJob.cancel()
    }

    @Test
    fun ListViewModel_test_get_note_success() = runTest {
        viewModel = ListViewModel(successMockRepository)
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiStateFlow.collect()
        }

        val curState = viewModel.uiStateFlow.value
        assert(curState is ListUiState.Success)
        collectJob.cancel()
    }

    @Test
    fun ListViewModel_test_get_empty_note() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel = ListViewModel(successMockRepository)
            viewModel.uiStateFlow.collect()
        }
        val curState = viewModel.uiStateFlow.value
        assert(curState is ListUiState.Success)
        assert((curState as ListUiState.Success).listNotes.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun ListViewModel_test_get_notes_failed() = runTest {
        `when`(errorMockRepository.getAllNoteEntities()).thenReturn(
            flow {
                emit(Result.Error(IllegalArgumentException("Error")))
            },
        )

        viewModel = ListViewModel(errorMockRepository)
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiStateFlow.collect()
        }
        val curState = viewModel.uiStateFlow.value
        assert(curState is ListUiState.Error)

        collectJob.cancel()
    }

    @Test
    fun ListViewModel_test_delete_note() = runTest {
        viewModel = ListViewModel(successMockRepository)
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiStateFlow.collect()
        }
        successMockRepository.upsertNote(noteFactory(1))
        val curState = viewModel.uiStateFlow.value

        assert(curState is ListUiState.Success)
        assert((curState as ListUiState.Success).listNotes.size == 1)

        successMockRepository.deleteNotes(listOf(1))
        val newState = viewModel.uiStateFlow.value
        assert((newState as ListUiState.Success).listNotes.isEmpty())

        collectJob.cancel()
    }

}

internal fun noteFactory(id: Int, title: String = "title") = Note(
    id = id,
    title = title,
    content = "content",
)