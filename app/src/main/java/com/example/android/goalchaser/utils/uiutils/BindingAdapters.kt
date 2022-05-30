/* Copyright 2022,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsRecyclerViewAdapter
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import com.example.android.goalchaser.utils.uiutils.MenuSelection
import com.example.android.goalchaser.utils.uiutils.setupDaysMonthsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@BindingAdapter("imageUrl")

fun bindImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl != null) {
        imgView.visibility = View.VISIBLE
        val lottieDrawable = LottieDrawable()
        LottieCompositionFactory.fromRawRes(imgView.context, R.raw.animated_image_loading)
            .addListener { lottieComposition ->
                lottieDrawable.apply {
                    composition = lottieComposition
                    repeatCount = LottieDrawable.INFINITE
                    playAnimation()

                }
            }
        val imgUri = imgUrl.toUri().buildUpon()?.scheme("https")?.build()
        Picasso.get()
            .load(imgUri)
            .fit()
            .centerInside()
            .placeholder(lottieDrawable)
            .error(R.drawable.image_error)
            .into(imgView)

    } else {
        imgView.visibility = View.GONE
    }
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

@BindingAdapter("showHideOnError")
fun ImageView.setImage(imgUrl: String?) {
    visibility = when (imgUrl) {
        null -> View.VISIBLE
        else -> View.GONE
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


@BindingAdapter("setupDate")
fun DatePicker.setupMinDateAndSaveGoalDueDate(dateToBeSaved: MutableLiveData<String?>) {
    val dateChangedListener =
        DatePicker.OnDateChangedListener { _: DatePicker, _: Int, _: Int, _: Int ->
            dateToBeSaved.value = context.getString(
                R.string.user_entered_date, month + 1, dayOfMonth, year
            )
        }
    init(year, month, dayOfMonth, dateChangedListener)
    // allow the user to set goal due date since tomorrow since today is already in progress
    //idea from here:https://stackoverflow.com/questions/43134925/
    // android-datepicker-dialog-date-should-come-from-tomorrow
    minDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
    val tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS)
    if (dateToBeSaved.value == null) {
        dateToBeSaved.value = context.getString(
            R.string.user_entered_date, tomorrow.monthValue, tomorrow.dayOfMonth, tomorrow.year
        )

    }
}

@BindingAdapter("isSwitched")

fun SwitchMaterial.getSwitchState(switchState: MutableLiveData<Boolean?>) {

    switchState.value?.let {
        isChecked = it
    }
    setOnCheckedChangeListener { _, _ ->
        switchState.value = isChecked
        val toastMessage = when (isChecked) {
            true -> context.getString(R.string.notifications_enabled)
            else -> context.getString(R.string.notifications_disabled)
        }
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}

//Adapter used only for create goal mode
//Used dummy parameter as BindingAdapter cannot have no arguments
@BindingAdapter("setupDaysMonths")
fun AutoCompleteTextView.setupDaysMonthsInitialValue(
    dummyParam: Nothing?
) {
    val daysMonthsArray = setupDaysMonthsAdapter()
    if (text.isNullOrEmpty()) {
        setText(daysMonthsArray[0], false)
    }
}

@BindingAdapter("isVisible")
fun View.setupVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("setImageResource")
fun ImageView.setImagePerMenuSelection(menuSelection: MenuSelection?) {
    val internalMenuSelection = menuSelection ?: MenuSelection.ACTIVE_GOALS
    setImageResource(
        when (internalMenuSelection) {
            MenuSelection.ACTIVE_GOALS -> R.drawable.no_goals_active
            MenuSelection.COMPLETED_GOALS -> R.drawable.no_goals_completed
        }
    )
}

@BindingAdapter("isAnimated")
fun LottieAnimationView.setAnimationStatus(animationPlayed: LiveData<Boolean>) {
    if (animationPlayed.value == true) {
        playAnimation()
    } else {
        setImageResource(R.drawable.goal_completed)
    }

}

@BindingAdapter("setVisibility")
fun FloatingActionButton.setupVisibility(activeCompletedGoalsMenuSelection: MenuSelection?) {
    visibility = when (activeCompletedGoalsMenuSelection) {
        MenuSelection.COMPLETED_GOALS -> View.GONE
        else -> View.VISIBLE
    }
}

@BindingAdapter("setGoalLabel")
fun TextView.setCompletedOrDueLabel(isGoalCompleted: Boolean) {
    text = context.getString(
        when (isGoalCompleted) {
            true -> R.string.completed_goal_label_list_item
            false -> R.string.due_goal_label_list_item
        }
    )
}

