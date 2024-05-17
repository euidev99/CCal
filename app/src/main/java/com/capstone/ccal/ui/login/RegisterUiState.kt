package com.capstone.ccal.ui.login

import android.net.Uri

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val nickname: String = "",
    val profileImageUri: Uri? = null
)
