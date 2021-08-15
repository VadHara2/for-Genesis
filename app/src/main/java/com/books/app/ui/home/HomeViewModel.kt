package com.books.app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject

class HomeViewModel:ViewModel() {

    val myJsonData = MutableLiveData<JSONObject>()
    val booksArray = MutableLiveData<JSONArray>()
    val topBannerSlidesArray = MutableLiveData<JSONArray>()
    val youWillLikeSection = MutableLiveData<JSONArray>()

    fun sortJson() {
        booksArray.value = myJsonData.value?.getJSONArray("books")
        topBannerSlidesArray.value = myJsonData.value?.getJSONArray("top_banner_slides")
        youWillLikeSection.value = myJsonData.value?.getJSONArray("you_will_like_section")
    }
}