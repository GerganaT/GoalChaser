package com.example.android.goalchaser.utils.datautils

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with an error message
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val message: String?) : Result<Nothing>()
}