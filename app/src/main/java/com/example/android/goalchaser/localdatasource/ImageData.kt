package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "image_data")
data class ImageData(

    val imageLink: String,
    val photographerName: String,
    val photographerProfile: String,
    val imageSavedDate:String = "",
    @PrimaryKey(autoGenerate = true) var ImageDataId: Int = 0,
)

//TODO remove last val if not needed