package com.books.app.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.books.app.data.BookItem
import com.books.app.databinding.GenreItemBinding
import com.bumptech.glide.Glide

class GenreAdapter(private val navigationBarHeight:Int) : ListAdapter<List<BookItem>, GenreAdapter.GenreViewHolder>(GenreComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null && this.itemCount == position) {
            holder.bind(currentItem, true, 0)
        }

        if (currentItem != null && this.itemCount != position) {
            holder.bind(currentItem, false, navigationBarHeight)
        }



    }

    class GenreViewHolder(val binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booksList:List<BookItem>, isLast:Boolean, navigationBarHeight: Int) {

            val booksAdapter = BookAdapter()

            binding.apply {
                recyclerViewBooks.apply {
                    adapter = booksAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }

                booksAdapter.submitList(booksList)
                genreName.text = booksList[0].genre

                if (isLast) {
                    navigationBar.layoutParams.height = navigationBarHeight
                    navigationBar.visibility = View.VISIBLE
                }

            }
        }

    }

    class GenreComparator : DiffUtil.ItemCallback<List<BookItem>>() {
        override fun areItemsTheSame(oldItem: List<BookItem>, newItem: List<BookItem>) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: List<BookItem>, newItem: List<BookItem>) =
            oldItem == newItem
    }
}