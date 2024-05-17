package com.capstone.ccal.ui.login

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


enum class RegisterStep {
    INPUT_EMAIL_PASS,
    INPUT_PROFILE,
    FINISH
}

class RegisterViewModel(
    private val registerRepo: RegisterRepository
): ViewModel() {

    val duplicatedRes: LiveData<Boolean> get() = registerRepo.duplicatedResult

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _currentStep = mutableStateOf(RegisterStep.INPUT_EMAIL_PASS)
    val currentStep: State<RegisterStep> = _currentStep

    val registerResult get() = registerRepo.registerResult

//    private val _checkDuplicatedState = mutableStateOf(false)

    val checkDuplicatedState: State<Boolean> get() = registerRepo.duplicateCheckedState
    val loadingProgressState: State<Boolean> get() = registerRepo.loadingProgressState
    val errorMessageOn: State<Boolean> get() = registerRepo.messageState

    val messageResult: State<String> get() = registerRepo.stringResult

    // 다음 단계로 이동하는 메서드
    fun nextStep() {
        _currentStep.value = when (_currentStep.value) {
            RegisterStep.INPUT_EMAIL_PASS -> RegisterStep.INPUT_PROFILE
            RegisterStep.INPUT_PROFILE -> RegisterStep.FINISH
            RegisterStep.FINISH -> RegisterStep.FINISH //그대로
        }
    }

    fun prevStep() {
        _currentStep.value = when (_currentStep.value) {
            RegisterStep.INPUT_EMAIL_PASS -> RegisterStep.INPUT_EMAIL_PASS //그대로
            RegisterStep.INPUT_PROFILE -> RegisterStep.INPUT_EMAIL_PASS
            RegisterStep.FINISH -> RegisterStep.INPUT_PROFILE
        }
    }

    //이메일 등록
    fun setEmailAndPassword(email: String, password: String) {
        Log.d("seki", "emailInput : $email, password : $password")
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                password = password
            )
        }
    }

    //닉네임, 프로필 이미지 등록imageUri: Uri,
    fun setProfile(nickName: String) {
        _uiState.update { currentState ->
            currentState.copy(
//                profileImageUri = imageUri,
                nickname = nickName
            )
        }
    }

    //이메일 중복 체크
    fun checkDuplicated(email: String) {
        viewModelScope.launch {
            registerRepo.checkDuplicated(email)
        }
    }

    //회원 등록
    fun register() {
        Log.d("seki", "viewmodel register")
        viewModelScope.launch {
            registerRepo.register(
                email = uiState.value.email,
                name = uiState.value.nickname,
                password = uiState.value.password,
                imageUri = null
            )
        }
    }

    companion object {
        fun provideFactory(
            registerRepo: RegisterRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(registerRepo) as T
            }
        }
    }
}
