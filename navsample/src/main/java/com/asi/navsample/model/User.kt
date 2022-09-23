package com.asi.navsample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @ClassName User.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 14:56:00
 */

@Parcelize
@kotlinx.serialization.Serializable
data class User(
    val name: String,
    val phone: String,
) : Parcelable