package com.ersiver.newsster.model

import android.os.Parcelable
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize

/**
 * Provides a source name for the article.
 */
@Parcelize
class Source(val name: String): Parcelable


/**
 * Convert the Source object to String so DB knows how to insert it.
 */
class SourceConverter{
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromString(value: String): Source?{
        val adapter: JsonAdapter<Source> = moshi.adapter(Source::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromSourceToString(type: Source): String{
        val adapter: JsonAdapter<Source> = moshi.adapter(Source::class.java)
        return adapter.toJson(type)
    }
}