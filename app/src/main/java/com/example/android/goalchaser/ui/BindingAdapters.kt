/**
Lottie animation license

MIT License
Copyright (c) 2019 LottieFiles.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/
package com.example.android.goalchaser.ui

import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.example.android.goalchaser.R
import com.example.android.goalchaser.localdatasource.ImageLocalData
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.lang.Exception

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    val lottieDrawable = LottieDrawable()
    LottieCompositionFactory.fromRawRes(imgView.context, R.raw.animated_image_loading)
        .addListener { lottieComposition ->
            lottieDrawable.apply {
                composition = lottieComposition
                repeatCount = LottieDrawable.INFINITE
                playAnimation()

            }
        }

    val imgUri = imgUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
    Picasso.get()
        .load(imgUri)
        .placeholder(lottieDrawable)
        .error(R.drawable.ic_image_error)
        .into(imgView)
//TODO error when no internet and no image in cache
}

@BindingAdapter("setPhotographerData")
fun TextView.showPhotographerCredentials(photographerCredentials: ImageDataUiState?) {

    val photographerName = photographerCredentials?.photographerName
    if (photographerName != null) {
        val photographersProfile = photographerCredentials.photographerProfile
        val unsplashSiteName = context.getString(R.string.unsplash_website_title)
        val unsplashSiteUrl = "https://unsplash.com"

        val photographerLabel = context.getString(
            R.string.photographer_credentials,
            "<a href=\"$photographersProfile\">$photographerName</a>",
            "<a href=\"$unsplashSiteUrl\">$unsplashSiteName</a>"
        )
        Linkify.addLinks(this, Linkify.ALL)
        text = Html.fromHtml(photographerLabel)
        movementMethod = LinkMovementMethod.getInstance()
    } else {
        text = context.getString(R.string.no_data_error)
    }


}