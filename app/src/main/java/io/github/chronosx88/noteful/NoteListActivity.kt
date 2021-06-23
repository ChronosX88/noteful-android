package io.github.chronosx88.noteful

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.github.chronosx88.noteful.models.Note
import java.util.*


class NoteListActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private val viewModel: NoteListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initActivity()
        initSorting()
        initRecyclerView()
    }

    private fun initActivity() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        drawer = findViewById(R.id.drawer_layout)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initSorting() {
        val items = listOf("Sort By Title", "Sort by Date Created", "Sort By Date Modified")
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item, items)
        val dropDownMenu: TextInputLayout = findViewById(R.id.sort_by_menu)
        val autoCompleteTV: AutoCompleteTextView = dropDownMenu.editText as AutoCompleteTextView
        autoCompleteTV.setAdapter(adapter)
        autoCompleteTV.setOnItemClickListener { _, _, position, _ ->
            viewModel.changeSortType(position)
        }
        autoCompleteTV.setText(items[viewModel.sortType], false)

        val sortDirectionButton: MaterialButton = findViewById(R.id.sort_direction_button)
        val changeSortDirectionIconButton: (Boolean) -> Unit = {
            if (it) sortDirectionButton.icon =
                AppCompatResources.getDrawable(this, R.drawable.ic_arrow_downward_black_24)
            else sortDirectionButton.icon =
                AppCompatResources.getDrawable(this, R.drawable.ic_arrow_upward_black_24)
        }
        changeSortDirectionIconButton(viewModel.sortDirection)
        sortDirectionButton.setOnClickListener {
            val dir = viewModel.changeSortDirection()
            changeSortDirectionIconButton(dir)
        }
    }

    fun initRecyclerView() {
        val noteList: RecyclerView = findViewById(R.id.note_list_rv)

        val adapter = NoteListAdapter { note ->
            val intent = Intent(this, NoteEditActivity::class.java)
            intent.putExtra("note", note)

            startActivity(intent)
        }
        noteList.adapter = adapter
        noteList.layoutManager = LinearLayoutManager(this)

        viewModel.notes.observe(this, Observer { notes ->
            adapter.data = notes.toList() as ArrayList<Note>
            adapter.notifyDataSetChanged()
        })


    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}