package com.jerboa.datatypes.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Register(
    val username: String,
    val password: String,
    val password_verify: String,
    val show_nsfw: Boolean,
    val email: String? = null,
    val captcha_uuid: String? = null,
    val captcha_answer: String? = null,
    val honeypot: String? = null,
    val answer: String? = null,
) : Parcelable