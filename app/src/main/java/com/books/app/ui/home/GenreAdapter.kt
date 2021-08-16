package com.books.app.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.books.app.data.BookItem
import com.books.app.data.SlideItem
import com.books.app.databinding.BannerItemBinding
import com.books.app.databinding.GenreItemBinding
import com.books.app.databinding.HeaderItemBinding
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_BANNER = 1
private const val ITEM_VIEW_TYPE_GENRE = 2
private const val TAG = "GenreAdapter"

class GenreAdapter(
    private val navigationBarHeight: Int,
    private val statusBarHeight: Int,
    private val slideListener: BannerAdapter.OnSlideClickListener,
    private val bookListener: BookAdapter.OnBookClickListener
) :
    ListAdapter<AdapterItem, RecyclerView.ViewHolder>(GenreComparator()) {

    fun submitMyCustomList(genreList: List<MutableList<BookItem>>, bannerList: List<SlideItem>) {
        val superList = mutableListOf<AdapterItem>()

        Log.i(TAG, "submitMyCustomList: ${bannerList}")

        superList.add(AdapterItem.Header("Library"))
        superList.add(AdapterItem.Banner(bannerList))

        for (genre in genreList) {
            superList.add(AdapterItem.GenreItem(genre))
        }
        submitList(superList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                HeaderItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            ITEM_VIEW_TYPE_BANNER -> BannerViewHolder(
                BannerItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            ITEM_VIEW_TYPE_GENRE -> GenreViewHolder(
                GenreItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdapterItem.Header -> ITEM_VIEW_TYPE_HEADER
            is AdapterItem.Banner -> ITEM_VIEW_TYPE_BANNER
            is AdapterItem.GenreItem -> ITEM_VIEW_TYPE_GENRE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(statusBarHeight)
            }
            is BannerViewHolder -> {
                val currentItem = getItem(position) as AdapterItem.Banner
                holder.bind(currentItem.slidesList)
            }
            is GenreViewHolder -> {
                val currentItem = getItem(position) as AdapterItem.GenreItem
                if (currentItem != null && this.itemCount == position) {
                    holder.bind(currentItem.list, true)
                }

                if (currentItem != null && this.itemCount != position) {
                    holder.bind(currentItem.list, false)
                }
            }
        }

    }

    class HeaderViewHolder(val binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(statusBarHeight: Int) {
            binding.statusBar.layoutParams.height = statusBarHeight
        }
    }

    inner class BannerViewHolder(val binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slidesList: List<SlideItem>) {
            val bannerAdapter = BannerAdapter(slidesList, slideListener)
            binding.apply {
                indicator.createIndicators(slidesList.size, 1)
                viewpager.apply {
                    adapter = bannerAdapter
                    viewpager.resumeAutoScroll()
                    onIndicatorProgress = { selectingPosition, progress ->
                        indicator.animatePageSelected(selectingPosition)
                    }
                }

            }
        }
    }

    inner class GenreViewHolder(val binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booksList: List<BookItem>, isLast: Boolean) {

            val booksAdapter = BookAdapter(bookListener)

            binding.apply {
                recyclerViewBooks.apply {
                    adapter = booksAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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


    class GenreComparator : DiffUtil.ItemCallback<AdapterItem>() {
        override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
            oldItem == newItem
    }


}

sealed class AdapterItem {

    data class GenreItem(val list: List<BookItem>) : AdapterItem() {
        override val id = list[0].genre
    }

    data class Header(val name: String) : AdapterItem() {
        override val id = "header"
    }

    data class Banner(val slidesList: List<SlideItem>) : AdapterItem() {
        override val id = slidesList.toString()
    }

    abstract val id: String
}