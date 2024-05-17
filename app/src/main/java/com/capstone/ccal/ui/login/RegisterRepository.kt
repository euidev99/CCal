package com.capstone.ccal.ui.login

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.CalApplication
import com.capstone.ccal.CalApplication.Companion.ApplicationContext
import com.capstone.ccal.R
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class RegisterRepository {

    enum class RegisterResult(val message: String) {
        IMAGE_SUCCESS("이미지 등록 성공"),
        SUCCESS(ApplicationContext().getString(R.string.success_user_registration_success)),
        FAILED(ApplicationContext().getString(R.string.error_user_registration_fail)),
        INVALID_EMAIL(ApplicationContext().getString(R.string.error_invalid_email_address)),
        DUPLICATED_EMAIL(ApplicationContext().getString(R.string.error_email_duplicated_)),
        SHORT_PASSWORD(ApplicationContext().getString(R.string.error_invalid_password_short)),
        DUPLICATED_EMAIL_COMPLETE(ApplicationContext().getString(R.string.message_email_duplicated_check)),
        ERROR_COMMON("에러가 발생했습니다.\n다시 시도해 주세요")
    }

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    private val _duplicatedResult = MutableLiveData<Boolean>()
    val duplicatedResult: LiveData<Boolean> get() = _duplicatedResult

    private var _loadingProgressState = mutableStateOf(false)
    val loadingProgressState: State<Boolean> = _loadingProgressState

    private var _duplicateCheckedState = mutableStateOf(false)
    val duplicateCheckedState: State<Boolean> = _duplicateCheckedState

    private val _stringResult = mutableStateOf<String>("")
    val stringResult: State<String> get() = _stringResult

    private var _messageState = mutableStateOf(false)
    val messageState: State<Boolean> = _messageState

    //중복 체크 로직
    suspend fun checkDuplicated(userEmail: String) {
        Log.d("seki", "checkDuplicated user email: $userEmail")

        _loadingProgressState.value = true
        if (isEmailValid(userEmail)) {
            val userRepository = BaseRepository(AppConst.FIREBASE.USER_INFO, UserDto::class.java)
            when (val result = userRepository.getDocumentsByField("email", userEmail)) {
                is RepoResult.Success -> {
                    Log.d("seki", "checkDuplicated success $result")
                    val dataList = result.data
                    if (dataList.isEmpty()) { // 없으면? 중복 아님
                        setErrorMessage(RegisterResult.DUPLICATED_EMAIL_COMPLETE.message)
                        _duplicatedResult.postValue(false)
                        _duplicateCheckedState.value = true
                    } else { // 있으면? 중복
                        Log.d("seki", "checkDuplicated fail duplicated email")
                        setErrorMessage(RegisterResult.DUPLICATED_EMAIL.message)
                        _duplicatedResult.postValue(true)
                        _duplicateCheckedState.value = true
//                        _stringResult.value = (RegisterResult.DUPLICATED_EMAIL.message)
                    }
//                    setErrorMessage("성공")
                    _loadingProgressState.value = false
                }

                is RepoResult.Error -> { //얜 그냥 실패
                    Log.d("seki", "checkDuplicated fail")
                    setErrorMessage(RegisterResult.ERROR_COMMON.message)
                    _duplicatedResult.postValue(false)
//                    _stringResult.value = (RegisterResult.ERROR_COMMON.message)

                    _loadingProgressState.value = false
                    _duplicateCheckedState.value = false
                }
                else -> {}
            }
        }
        else {
//            _stringResult.value = (RegisterResult.INVALID_EMAIL.message)
            setErrorMessage(RegisterResult.INVALID_EMAIL.message)
            _loadingProgressState.value = false
            _duplicateCheckedState.value = false
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    private fun isPasswordInValid(text: String): Boolean {
        return text.length < 6
    }

    private val supervisorJob = SupervisorJob()

    private suspend fun setErrorMessage(message: String) {
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

    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadImageToFirebase(imageUri: Uri, userEmail: String) {
        try {
            // Firebase Storage에 이미지 업로드
            val imageName = "$userEmail.jpg"
            val imageRef = storage.reference.child("profile/$imageName")
            val uploadTask = imageRef.putFile(imageUri)
            val downloadUrl = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.await()

            // Firestore에 데이터 추가
//            val imageData = ImageData(downloadUrl.toString(), "Image Description")
//            firestore.collection("images").add(imageData)


        } catch (e: Exception) {
            //암튼 실패
        }
    }

    //계정에 사용자 등록 후, 성공시 정보를 DB 에 추가로 저장하도록 함
    //만약 실패시, 그냥 다 실패로 하고 리트라이를 시도하는 것으로 예외처리하지 않음.
    suspend fun register(email: String, name: String, password: String, imageUri: Uri?) {

        _loadingProgressState.value = true

        if (isPasswordInValid(password)) {
            _loadingProgressState.value = false
            setErrorMessage(RegisterResult.SHORT_PASSWORD.message)
            return
        }

        if (imageUri != null) {
            try {
                //새로운 사용자 만들기
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val userRepository = BaseRepository(AppConst.FIREBASE.USER_INFO, UserDto::class.java)
                            CoroutineScope(Dispatchers.IO).launch {

                                val user = mAuth.currentUser
                                val uid = user!!.uid

                                val imageName = UUID.randomUUID().toString() + ".jpg"
                                val imageRef = storage.reference.child("profile/$imageName")
                                val uploadTask = imageRef.putFile(imageUri)
                                val downloadUrl = uploadTask.continueWithTask { task ->
                                    if (!task.isSuccessful) {
                                        task.exception?.let { throw it }
                                    }
                                    imageRef.downloadUrl
                                }.await()

                                when (val result = userRepository.addDocumentWithId(email, UserDto(email, name, password, imageName))) {
                                    is RepoResult.Success -> {
                                        //이메일 방식으로 변경
//                                        setUserPointWithEmail(email)
//                                        setUserAdListWithId(email)

                                        _loadingProgressState.value = false
                                    }

                                    is RepoResult.Error -> {
                                        _registerResult.postValue(false)
                                        _loadingProgressState.value = false
                                        Log.d("seki", "계정 등록 실패 : " + result.exception.toString())
                                    }
                                }
                            }
                        } else {
                            // 가입 실패
                            _registerResult.postValue(false)
                            _loadingProgressState.value = false
                            Log.d("seki", "등록 실패 : " + task.exception)
                        }
                    }

            } catch (e: Exception) {
                _loadingProgressState.value = false
                _registerResult.postValue(false)
                Log.d("seki", "등록 실패 : " + e.toString())
            }
        } else {
            //새로운 사용자 만들기 이미지 없이
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userRepository =
                            BaseRepository(AppConst.FIREBASE.USER_INFO, UserDto::class.java)
                        CoroutineScope(Dispatchers.IO).launch {
                            when (val result = userRepository.addDocumentWithId(
                                email,
                                UserDto(email, name, password)
                            )) {
                                is RepoResult.Success -> {
                                    //이메일 방식으로 변경
//                                    setUserPointWithEmail(email)
//                                    setUserAdListWithId(email)
                                    Log.d("seki", "등록 성공 : " )
                                    setErrorMessage(RegisterResult.SUCCESS.message)
                                    delay(3000) //화면 이동할때 좀 그래서
                                    _registerResult.postValue(true)
                                    _loadingProgressState.value = false
                                }

                                is RepoResult.Error -> {
                                    _registerResult.postValue(false)
                                    setErrorMessage(RegisterResult.FAILED.message)
                                    Log.d("seki", "등록 실패 : " + result.exception.toString())
                                    _loadingProgressState.value = false
                                }
                            }
                        }
                    } else {
                        // 가입 실패
                        _registerResult.postValue(false)
                        Log.d("seki", "등록 실패 : " + task.exception)
                        _loadingProgressState.value = false
                    }
                }
        }
    }
}