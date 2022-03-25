package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_data")
data class ImageData(

    val imageLink: String,
    val photographerName: String,
    val photographerProfile: String,
    @PrimaryKey(autoGenerate = true) var ImageDataId: Int = 0,
)

