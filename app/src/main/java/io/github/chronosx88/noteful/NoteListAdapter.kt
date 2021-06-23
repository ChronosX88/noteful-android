package io.github.chronosx88.noteful

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.chronosx88.noteful.models.Note
import java.util.*

class NoteListAdapter(val listener: (Note)->Unit): RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    var data = ArrayList<Note>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteName: TextView = view.findViewById(R.id.note_list_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteName.text = data[position].title
        holder.itemView.setOnClickListener {
            listener(data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}