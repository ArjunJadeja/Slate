package com.arjun.slate.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.slate.R
import com.arjun.slate.data.Text
import com.google.android.material.button.MaterialButton

class TextRVAdapter(private val context: Context, private val listener: INotesRVAdapter) :
    RecyclerView.Adapter<TextRVAdapter.TextViewHolder>() {

    private var textsList = emptyList<Text>()

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text)
        val postTime: TextView = itemView.findViewById(R.id.postingTimeTextView)
        val shareButton: MaterialButton = itemView.findViewById(R.id.shareTextButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val viewHolder = TextViewHolder(
            LayoutInflater.from(context).inflate(R.layout.text_list, parent, false)
        )
        viewHolder.shareButton.setOnClickListener {
            listener.onItemClicked(textsList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentText = textsList[position]
        holder.textView.text = currentText.text
        holder.postTime.text = PostingTimeFormat.getPostTime(currentText.postingTime)
    }

    override fun getItemCount(): Int {
        return textsList.size
    }

    fun updateList(newTextsList: List<Text>) {
        val diffUtil = TextsListDiffUtil(textsList, newTextsList)
        textsList = newTextsList
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        diffResults.dispatchUpdatesTo(this)
    }
}

interface INotesRVAdapter {
    fun onItemClicked(text: Text)
}