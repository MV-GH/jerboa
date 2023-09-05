package com.jerboa.datatypes.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatePostReport(
    val post_id: PostId,
    val reason: String,
    val auth: String,
) : Parcelable