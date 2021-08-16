package com.books.app.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookItem(
    val id :Int,
    val name: String,
    val author: String,
    val summary: String,
    val genre: String,
    val cover_url: String,
    val views: String,
    val likes: String,
    val quotes: String,
) : Parcelable {
}