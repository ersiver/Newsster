package com.ersiver.newsster.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ersiver.newsster.R


/**
 *  Helper function that removes time from a given date-time String.
 */
fun String.formatDate(): String {
    var toBeReturned = this
    val key = "T"

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

/**
 *  Helper function that removes unnecessary text from the article content.
 */
fun String.formatContent(): String {
    var toBeReturned = this
    val key = "["

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

/**
 *  Helper function that removes unnecessary text from the article title.
 */
fun String.formatTitle(): String {
    var toBeReturned = this
    val key = " - "

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

/**
 *  Helper function that either load text into the
 *  TextView or hides the view, if the text is empty
 */
fun TextView.loadOrGone(dataText: String) {
    if (dataText.isNotEmpty())
        text = dataText
    else this.isGone = true
}

/**
 *  Helper function that either loads article image
 *  or loads the default logo, if the image url is
 *  not available.
 */
fun ImageView.loadImageOrDefault(imgUrl: String) {
    if (imgUrl.isNotEmpty())
        Glide.with(context)
            .load(imgUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_logo))
            .into(this)
    else
        this.setImageResource(R.drawable.newsster_logo)
}