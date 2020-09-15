package com.ersiver.newsster.util

import androidx.test.espresso.idling.net.UriIdlingResource

/**
 * Implementation of UriIdlingResource that determines
 * idleness by maintaining an internal counter. The counter
 * needs to be zero for a specific period of time before the
 * resource is considered idle. This additional waiting period
 * takes consecutive network requests into account.
 */
object EspressoUriIdlingResource{

    private const val RESOURCE = "DATA_LOADED"
    private const val TIMEOUT_MS = 2000L

    @JvmField
    val uriIdlingResource = UriIdlingResource(RESOURCE, TIMEOUT_MS)

    fun beginLoad(){
        uriIdlingResource.beginLoad(BASE_URL)
    }

    fun endLoad(){
        uriIdlingResource.endLoad(BASE_URL)
    }
}