package io.github.chronosx88.noteful

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import io.github.chronosx88.noteful.editHandlers.*
import io.github.chronosx88.noteful.models.Note
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.editor.handler.EmphasisEditHandler
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import java.util.concurrent.Executors


class NoteEditActivity : AppCompatActivity() {
    private val viewModel: NoteEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        initActivity()
    }

    private fun initActivity() {
        // showing the back button in action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        val note = intent.extras?.get("note") as Note
        actionBar?.title = note.title

        val markwon : Markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .usePlugin(TaskListPlugin.create(this))
            .build();
        val editor = MarkwonEditor.builder(markwon)
            .useEditHandler(EmphasisEditHandler())
            .useEditHandler(StrongEmphasisEditHandler())
            .useEditHandler(StrikethroughEditHandler())
            .useEditHandler(CodeEditHandler())
            .useEditHandler(BlockQuoteEditHandler())
            .useEditHandler(LinkEditHandler { _, _ ->
                // FIXME need to fix movement method as it doesn't support correctly edittext
            })
            .useEditHandler(HeadingEditHandler())
            .build()
        val editText: EditText = findViewById(R.id.note_edit_text)

//        editText.setMovementMethod(BetterLinkMovementMethod.getInstance()); // FIXME
        editText.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(editor, Executors.newCachedThreadPool(), editText));
        editText.setText("**Hello World!**")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}