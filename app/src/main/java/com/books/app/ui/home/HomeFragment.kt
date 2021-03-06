package com.books.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.books.app.R
import com.books.app.data.BookItem
import com.books.app.data.SlideItem
import com.books.app.databinding.FragmentHomeBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), BannerAdapter.OnSlideClickListener,
    BookAdapter.OnBookClickListener {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val genreAdapter = GenreAdapter(getNavigationBarHeight(), getStatusBarHeight(), this, this)
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Toast.makeText(
                        requireContext(), "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.myJsonData.value = JSONObject(remoteConfig.get("json_data").asString())

                } else {
                    Toast.makeText(
                        requireContext(), "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        viewModel.myJsonData.observe(requireActivity(), Observer {
            viewModel.sortJson()
        })

        binding.apply {
            recyclerView.apply {
                adapter = genreAdapter
                layoutManager = LinearLayoutManager(requireContext())

                viewModel.genreLists.observe(requireActivity(), Observer { result ->

                    genreAdapter.submitMyCustomList(
                        result.values.toList(),
                        viewModel.bannerList.value ?: listOf(SlideItem(-1, -1, "-1"))
                    )
                })

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

    private fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onSlideClick(bookId: Int) {
        val bookInfo = viewModel.booksArray.value?.get(bookId) as JSONObject
        val currentBook = BookItem(
            id = bookInfo.getInt("id"),
            name = bookInfo.getString("name"),
            author = bookInfo.getString("author"),
            summary = bookInfo.getString("summary"),
            genre = bookInfo.getString("genre"),
            cover_url = bookInfo.getString("cover_url"),
            views = bookInfo.getString("views"),
            likes = bookInfo.getString("likes"),
            quotes = bookInfo.getString("quotes")
        )

        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                currentBook,
                viewModel.recommendedBooks.value!!.toTypedArray()
            )
        )
    }

    override fun onBoolClick(bookInfo: BookItem) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                bookInfo,
                viewModel.recommendedBooks.value!!.toTypedArray()
            )
        )
    }

}