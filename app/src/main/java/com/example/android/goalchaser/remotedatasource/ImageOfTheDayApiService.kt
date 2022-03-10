package com.example.android.goalchaser.remotedatasource

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


private const val BASE_URL = "https://api.unsplash.com/"


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ImageDataApiService {



   //TODO Insert key here -hidden for security purposes
    @GET("photos/random?collections=ssKIeMdFdRo")
    suspend fun getImageOfTheDay(): ImageOfTheDayData?

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ImageOfTheDayApiService {
    val retrofitService: ImageDataApiService by lazy { retrofit.create(ImageDataApiService::class.java) }
}
//TODO obtain photographer's profile link and Unsplash's site