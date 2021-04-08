package com.example.reddittest.ui.main.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RedditQueryDataInside(
    @SerializedName(value = "thumbnail")
    val thumbnail: String? = null,
    @SerializedName(value = "url_overridden_by_dest")
    val urlOverridenByDest: String? = null,
    @SerializedName(value = "title")
    val title: String? = null,
    @SerializedName(value = "subreddit_name_prefixed")
    val namePrefixed: String? = null
) : Serializable

data class RedditQueryThread(val data: RedditQueryDataInside? = null) : Serializable
data class RedditQueryBodyResponse(val children: ArrayList<RedditQueryThread>?) : Serializable
data class RedditQueryResponse(val data: RedditQueryBodyResponse) : Serializable