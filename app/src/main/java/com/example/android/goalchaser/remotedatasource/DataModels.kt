package com.example.android.goalchaser.remotedatasource

/** Data models,which Moshi uses to convert image-related data from the Web
 * to Kotlin objects, which we can use in the app*/
data class ImageOfTheDayData(
    val urls: RandomImage,
    val user: User
) {
}

data class RandomImage(
    val small:String
)

data class User(
    val name:String
)