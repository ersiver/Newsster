package com.ersiver.newsster.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "article_table")
class Article (
    @PrimaryKey
    val id: String,
    val url: String,
    val author: String,
    val title: String,
    val description: String,
    val imgUrl: String,
    val date: String,
    val content: String,
    val source: Source,
    var category: String = "",
    var language: String = ""
): Parcelable