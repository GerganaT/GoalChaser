package com.example.android.goalchaser.utils.uiutils

import java.io.Serializable

enum class MenuSelection:Serializable {
    ACTIVE_GOALS,
    COMPLETED_GOALS,
    DELETE_ACTIVE_GOALS,
    DELETE_COMPLETED_GOALS,
    DELETE_ALL_GOALS
}