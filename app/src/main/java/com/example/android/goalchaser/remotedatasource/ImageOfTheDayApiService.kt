package com.example.android.goalchaser

import com.example.android.goalchaser.remotedatasource.ImageOfTheDayData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


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

/**
 * A public interface that exposes the [getQuoteOfTheDay] method
 */
interface TestApiService {
    /**
     * Returns a Coroutine [List] of [testquote] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */


   //TODO Insert key here -hidden for security purposes
    @GET("photos/random?collections=ssKIeMdFdRo")
    suspend fun getImageOfTheDay(): ImageOfTheDayData?
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ImageOfTheDayApiService {
    val retrofitService: TestApiService by lazy { retrofit.create(TestApiService::class.java) }
}
