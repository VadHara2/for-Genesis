package com.books.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.books.app.R
import com.books.app.data.SlideItem
import com.bumptech.glide.Glide

class BannerAdapter(
    private val slidesList: List<SlideItem>,
    private val listener: OnSlideClickListener
) : LoopingPagerAdapter<SlideItem>(slidesList, true) {


    override fun bindView(convertView: View, listPosition: Int, viewType: Int) {
        val mySlide = convertView.findViewById<ImageView>(R.id.slide_img)
        convertView.setOnClickListener {
            listener.onSlideClick(slidesList[listPosition].bookId)
        }
        Glide.with(mySlide)
            .load(slidesList[listPosition].cover)
            .into(mySlide)
    }

    override fun inflateView(viewType: Int, container: ViewGroup, listPosition: Int): View {
        return LayoutInflater.from(container.context).inflate(R.layout.slide_item, container, false)
    }

    interface OnSlideClickListener {
        fun onSlideClick(bookId: Int)
    }
}

