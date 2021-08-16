package com.books.app.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.books.app.data.BookItem
import com.books.app.data.SlideItem
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    val myJsonData = MutableLiveData<JSONObject>()
    val booksArray = MutableLiveData<JSONArray>()
    val topBannerSlidesArray = MutableLiveData<JSONArray>()
    val youWillLikeSection = MutableLiveData<JSONArray>()
    val genreLists = MutableLiveData<MutableMap<String, MutableList<BookItem>>>()
    val bannerList = MutableLiveData<List<SlideItem>>()

    fun sortJson() {
        booksArray.value = myJsonData.value?.getJSONArray("books")
        topBannerSlidesArray.value = myJsonData.value?.getJSONArray("top_banner_slides")
        youWillLikeSection.value = myJsonData.value?.getJSONArray("you_will_like_section")

        val numberOfBooks = booksArray.value?.length()
        val numberOfSlides = topBannerSlidesArray.value?.length()
        if (numberOfBooks != null) {
            val existingGenre = mutableSetOf<String>()
            var newGenreList = mutableMapOf<String, MutableList<BookItem>>()
            for (bookId in 1 until numberOfBooks) {
                val bookInfo = booksArray.value?.get(bookId) as JSONObject
                val genreString = bookInfo.getString("genre")
                Log.i(TAG, "sortJson: ${genreString}")

                if (existingGenre.contains(genreString)) {
                    newGenreList["$genreString"]?.add(
                        BookItem(
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
                    )
                } else {
                    newGenreList["$genreString"] = mutableListOf(
                        BookItem(
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
                    )
                    existingGenre.add(genreString)
                }
                genreLists.value = newGenreList
            }
        }

        if (numberOfSlides != null) {
            val newSlidesList = mutableListOf<SlideItem>()
            for (slidesId in 0 until numberOfSlides) {
                val slideInfo = topBannerSlidesArray.value?.get(slidesId) as JSONObject
                val imageString = slideInfo.getString("cover")
                val id = slideInfo.getInt("id")
                val bookId = slideInfo.getInt("book_id")
                newSlidesList.add(SlideItem(id, bookId, imageString))
            }
            bannerList.value = newSlidesList
        }


    }
}