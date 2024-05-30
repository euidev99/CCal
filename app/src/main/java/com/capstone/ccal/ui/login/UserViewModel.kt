package com.capstone.ccal.ui.login

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.common.UserRepository
import com.capstone.ccal.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    enum class LoginResult(val message: String) {
        IMAGE_SUCCESS("이미지 등록 성공"),
        SUCCESS(CalApplication.ApplicationContext().getString(R.string.success_user_registration_success)),
        FAILED(CalApplication.ApplicationContext().getString(R.string.login_message_failed)),
        INVALID_EMAIL(CalApplication.ApplicationContext().getString(R.string.error_invalid_email_address)),
        DUPLICATED_EMAIL(CalApplication.ApplicationContext().getString(R.string.error_email_duplicated_)),
        SHORT_PASSWORD(CalApplication.ApplicationContext().getString(R.string.error_invalid_password_short)),
        DUPLICATED_EMAIL_COMPLETE(CalApplication.ApplicationContext().getString(R.string.message_email_duplicated_check)),
        ERROR_COMMON(CalApplication.ApplicationContext().getString(R.string.login_message_error))
    }

    //FireBase 인증
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email


    fun setUser(username: String, email: String) {
        _username.value = username
        _email.value = email
    }

    private var _messageState = mutableStateOf(false)
    val messageState: State<Boolean> = _messageState

    private val _stringResult = mutableStateOf<String>("")
    val stringResult: State<String> get() = _stringResult

    private var _loadingProgressState = MutableLiveData<Boolean>()
    val loadingProgressState: LiveData<Boolean> get() = _loadingProgressState

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun logIn(email: String, passwd: String) {
        _loadingProgressState.postValue(true)

        //입력 없을 때
        if (email.isEmpty()) {
            viewModelScope.launch {
                setErrorMessage("이메일을 입력해주세요")
            }
            return
        }
        if (passwd.isEmpty()) {
            viewModelScope.launch {
                setErrorMessage("비밀번호를 입력해주세요")
            }
            return
        }
        mAuth.signInWithEmailAndPassword(email, passwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val user = mAuth.currentUser
                    val userRepository =
                        BaseRepository(AppConst.FIREBASE.USER_INFO, UserDto::class.java)
                    viewModelScope.launch {
                        when (val result = userRepository.getDocumentsByField("email", email)) {
                            is RepoResult.Success -> {
                                val dataList = result.data
                                if (dataList.isEmpty()) {
                                    _loginResult.postValue(false)
                                    setErrorMessage(LoginResult.FAILED.message)
                                    Log.d("login", "fail no user $result")
                                    return@launch
                                }

                                if (dataList[0].password == passwd) {
                                    //Login Success
                                    //간소화 처리
                                    UserRepository.saveEmail(
                                        CalApplication.ApplicationContext(),
                                        dataList[0].email
                                    )
                                    UserRepository.saveUsername(
                                        CalApplication.ApplicationContext(),
                                        dataList[0].name
                                    )

                                    Log.d("login", "success $result")
                                    _loginResult.postValue(true)
                                    setErrorMessage(LoginResult.SUCCESS.message)
                                } else {
                                    Log.d("seki", "Password Error $result")
                                    _loginResult.postValue(false)
                                    setErrorMessage(LoginResult.FAILED.message)
                                }
                            }

                            is RepoResult.Error -> {
                                //Error
                                _loginResult.postValue(false)
                                setErrorMessage(LoginResult.ERROR_COMMON.message)
                                Log.d("seki", result.exception.toString())
                            }
                        }
                    }
                } else {
                    // 로그인 실패
                    Log.d("seki", "실패 : " + task.exception.toString())

                    _loginResult.postValue(false)
                    viewModelScope.launch {
                        setErrorMessage(LoginResult.ERROR_COMMON.message)
                    }
                }
            }
    }

    private val supervisorJob = SupervisorJob()

    private suspend fun setErrorMessage(message: String) {
        _loadingProgressState.postValue(false)
        val coroutineScope = CoroutineScope(supervisorJob + Dispatchers.Default)

        // 이전에 실행 중인 작업이 있다면 취소합니다.
        coroutineScope.coroutineContext.cancelChildren()

        coroutineScope.launch {
            _messageState.value = true
            _stringResult.value = message
            delay(3000)
            _messageState.value = false
            _stringResult.value = ""
        }
    }

    companion object {
        fun provideFactory(

        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel() as T
            }
        }
    }
}
