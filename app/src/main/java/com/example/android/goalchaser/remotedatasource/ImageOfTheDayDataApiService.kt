package com.example.android.goalchaser.remotedatasource

import com.example.android.goalchaser.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


private const val BASE_URL = "https://api.unsplash.com/"
private const val API_KEY = BuildConfig.API_KEY

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ImageDataApiService {

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("photos/random?collections=ssKIeMdFdRo")
    suspend fun getImageOfTheDayData(): ImageRemoteData

}

object ImageOfTheDayDataApiService {
    val retrofitService: ImageDataApiService by lazy { retrofit.create(ImageDataApiService::class.java) }
}
