package com.example.android.goalchaser.remotedatasource

import com.squareup.moshi.Json

data class ImageOfTheDayDataRemote(
    @Json(name = "urls") val randomImage: RandomImage,
    @Json(name = "user") val photographer: Photographer
) {
}

data class RandomImage(
    @Json(name = "small") val imageLink: String? = "" +"&fit=clamp"
)

data class Photographer(
    val name: String?,
    @Json(name = "links") val profileLinks: PhotographerProfile
)

data class PhotographerProfile(
    @Json(name = "html") val profileLink: String?
)

