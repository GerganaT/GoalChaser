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
import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsRecyclerViewAdapter
import com.example.android.goalchaser.ui.createeditgoal.CreateEditGoalViewModel
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import com.google.android.material.switchmaterial.SwitchMaterial
import com.squareup.picasso.Picasso
import timber.log.Timber

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
    //TODO delete older images - set some bool on old images to mark them as old
    // this will include database items deletion as well.
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

@Suppress("UNCHECKED_CAST")
@BindingAdapter("feedLiveData")

fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
    items?.value?.let { itemList ->
        (recyclerView.adapter as? GoalsRecyclerViewAdapter<T>)?.apply {
            clear()
            addData(itemList)


        }
    }
}

@BindingAdapter("insertDate")

fun DatePicker.saveDate(dateToBeSaved: MutableLiveData<String>) {
    //month + 1 used to match the month name on the dialog to the month number we will display
    // in the goals' list once the goal is saved.
    dateToBeSaved.value = context.getString(
        R.string.user_entered_date, month + 1, dayOfMonth, year
    )

}

@BindingAdapter("isSwitched")

fun SwitchMaterial.getSwitchState(switchState: MutableLiveData<Boolean>) {
    switchState.value = isChecked

}



@BindingAdapter("setupDaysMonthsCount")
fun AutoCompleteTextView.setupDaysMonthsCountAdapter(days: Array<Int>) {
    val daysNumberAdapter = ArrayAdapter(
        context, R.layout.days_months_dropdown_menu_item,
        days
    )
    setAdapter(daysNumberAdapter)
    setText(days[0].toString(), false)
}

//Used dummy parameter as BindingAdapter cannot have no arguments
@BindingAdapter("setupDaysMonths")
fun AutoCompleteTextView.setupDaysMonthsAdapter(dummyParam:Nothing?

) {
    val dayMonthsArray = context.resources.getStringArray(R.array.days_months)
    val daysMonthsAdapter = ArrayAdapter(
        context,
        R.layout.days_months_dropdown_menu_item,
        dayMonthsArray
    )
    setAdapter(daysMonthsAdapter)
    setText(dayMonthsArray[0],false)
}

