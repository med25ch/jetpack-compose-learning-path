package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

// ---------- Data model ----------
data class Note(val id: String, val title: String, val body: String)

// ---------- Top-level screen (UI shell only) ----------
// Stateless: every piece of state/behaviour is passed in.
// The real NotesScreen(repository) will own state + effects and delegate to this.
@Composable
fun NotesScreenContent(
    isLoading: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    resultsLabel: String,                 // e.g. "3 notes" — precomputed by the owner
    notes: List<Note>,                    // filtered list, passed in
    selectedNote: Note?,
    onNoteClick: (Note) -> Unit,
    editedBody: String,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(query = query, onQueryChange = onQueryChange)

        if (isLoading) {
            Text("Loading…")
        } else {
            ResultsCount(label = resultsLabel)
            NoteList(
                notes = notes,
                onNoteClick = onNoteClick
            )
            if (selectedNote != null) {
                NoteEditor(
                    body = editedBody,
                    onBodyChange = onBodyChange
                )
            }
        }
    }
}

// ---------- Children (all stateless, data down / events up) ----------

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
    )
}

@Composable
fun ResultsCount(
    label: String,
    modifier: Modifier = Modifier
) {
    Text(text = label, modifier = modifier)
}

@Composable
fun NoteList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        notes.forEach { note ->
            NoteRow(note = note, onClick = { onNoteClick(note) })
        }
    }
}

@Composable
fun NoteRow(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Text(note.title)
    }
}

@Composable
fun NoteEditor(
    body: String,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = body,
        onValueChange = onBodyChange,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun NotesScreenContentPreview() {
    val dummyNotes = listOf(
        Note(id = "1", title = "Groceries", body = "Milk, eggs, bread, coffee"),
        Note(id = "2", title = "Meeting notes", body = "Discuss Q4 roadmap and hiring"),
        Note(id = "3", title = "Book ideas", body = "Sci-fi about a lost colony ship"),
        Note(id = "4", title = "Workout", body = "Push day: bench, shoulders, triceps")
    )

    NotesScreenContent(
        isLoading = false,
        query = "co",
        onQueryChange = {},                 // no-op — preview has no state
        resultsLabel = "${dummyNotes.size} notes",
        notes = dummyNotes,
        selectedNote = dummyNotes[1],       // simulate a selected note → editor shows
        onNoteClick = {},
        editedBody = dummyNotes[1].body,    // editor pre-filled with the selected note's body
        onBodyChange = {}
    )
}