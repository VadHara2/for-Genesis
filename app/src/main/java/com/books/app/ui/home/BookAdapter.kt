package com.books.app.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.books.app.data.BookItem
import com.books.app.databinding.BookItemBinding
import com.bumptech.glide.Glide

class BookAdapter(private val listener: OnBookClickListener, private val isTextBlack: Boolean) :
    ListAdapter<BookItem, BookAdapter.BookViewHolder>(BookComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class BookViewHolder(val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookItem) {
            binding.apply {

                root.setOnClickListener {
                    listener.onBoolClick(book)
                }

                Glide.with(bookImg)
                    .load(book.cover_url)
                    .into(bookImg)

                bookName.text = book.name

                if (isTextBlack) {
                    bookName.setTextColor(Color.parseColor("#393637"))
                    bookName.alpha = 1f
                }
            }
        }

    }

    class BookComparator : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem) =
            oldItem == newItem
    }

    interface OnBookClickListener {
        fun onBoolClick(bookInfo: BookItem)
    }
}