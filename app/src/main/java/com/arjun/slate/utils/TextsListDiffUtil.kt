package com.arjun.slate.utils

import androidx.recyclerview.widget.DiffUtil
import com.arjun.slate.data.Text

class TextsListDiffUtil(
    private val oldList: List<Text>,
    private val newList: List<Text>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id != newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].text != newList[newItemPosition].text -> {
                false
            }
            oldList[oldItemPosition].postingTime != newList[newItemPosition].postingTime -> {
                false
            }
            else -> true
        }
    }
}