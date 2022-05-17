package com.example.android.goalchaser.utils.uiutils

import com.google.android.material.snackbar.Snackbar
import java.io.Serializable


data class SavingSnackbar(
    val savedSnackbar: Snackbar?
) : Serializable
