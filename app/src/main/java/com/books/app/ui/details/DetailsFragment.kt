package com.books.app.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.books.app.R
import com.books.app.data.BookItem
import com.books.app.databinding.FragmentDetailsBinding
import com.books.app.ui.home.BookAdapter
import com.bumptech.glide.Glide

class DetailsFragment:Fragment(R.layout.fragment_details), BookAdapter.OnBookClickListener {

    private val args : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        val myAdapter = BookAdapter(this, true)
        binding.apply {
            statusBar.layoutParams.height = getStatusBarHeight()
            navigationBar.layoutParams.height = getNavigationBarHeight()
            Glide.with(bookImg)
                .load(args.bookInfo.cover_url)
                .into(bookImg)
            book = args.bookInfo

            backBtn.setOnClickListener { findNavController().popBackStack() }

            recyclerViewBooks.apply {
                adapter = myAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                myAdapter.submitList(args.youWillLikeList.toList())

            }
        }

        return binding.root
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getNavigationBarHeight():Int {
        var result = 0
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onBoolClick(bookInfo: BookItem) {
        Toast.makeText(requireContext(),"${bookInfo.name}", Toast.LENGTH_SHORT).show()
    }
}