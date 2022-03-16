package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_data")
data class ImageData(
    @PrimaryKey(autoGenerate = true) val ImageDataId: Int,
    val imageLink: String?,
    val photographerName: String?,
    val photographerProfile: String?
)

