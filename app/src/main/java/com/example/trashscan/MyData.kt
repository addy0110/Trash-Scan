package com.example.trashscan

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyData(val bitmap: Bitmap, val result: String): Parcelable