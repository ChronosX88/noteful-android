package io.github.chronosx88.noteful

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.chronosx88.noteful.models.Note
import kotlinx.coroutines.launch

class NoteListViewModel: ViewModel() {
    private val _notes = MutableLiveData<Array<Note>>()
    val notes : LiveData<Array<Note>> = _notes
    val notesArray = arrayOf(
        Note(
            title = "Simple Note",
            createdAt = 0,
            modifiedAt = 0,
            tags = listOf(),
            attachments = listOf(),
            favorite = false,
            pinned = false,
            additionalData = mapOf()
        ),
        Note(
            title = "Yet Another Simple Note",
            createdAt = 3,
            modifiedAt = 4,
            tags = listOf(),
            attachments = listOf(),
            favorite = false,
            pinned = false,
            additionalData = mapOf()
        ),
        Note(
            title = "Another Simple Note",
            createdAt = 2,
            modifiedAt = 2,
            tags = listOf(),
            attachments = listOf(),
            favorite = false,
            pinned = false,
            additionalData = mapOf()
        )
    )
    var sortDirection = true
        private set
    var sortType = 0
        private set

    init {
        sortNotes()
        viewModelScope.launch {
            _notes.value = notesArray
        }
    }

    fun changeSortType(sortTypeItem: Int) {
        sortType = sortTypeItem
    }

    fun changeSortDirection(): Boolean {
        sortDirection = !sortDirection
        sortNotes()
        viewModelScope.launch {
            _notes.value = notesArray
        }
        return sortDirection
    }

    private fun sortNotes() {
        if (sortDirection) {
            when(sortType) {
                0 -> {
                    notesArray.sortBy { note -> note.title }
                }
                1 -> {
                    notesArray.sortBy { note -> note.createdAt }
                }
                2 -> {
                    notesArray.sortBy { note -> note.modifiedAt }
                }
            }
        } else {
            when(sortType) {
                0 -> {
                    notesArray.sortByDescending { note -> note.title }
                }
                1 -> {
                    notesArray.sortByDescending { note -> note.createdAt }
                }
                2 -> {
                    notesArray.sortByDescending { note -> note.modifiedAt }
                }
            }
        }
    }
}