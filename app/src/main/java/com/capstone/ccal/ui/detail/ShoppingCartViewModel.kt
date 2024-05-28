package com.capstone.ccal.ui.detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.ui.login.RegisterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShoppingCartViewModel(
    private val repo: RegisterRepository
) : ViewModel() {

    enum class ShoppingCartResult(val message: String) {
        SUCCESS(CalApplication.ApplicationContext().getString(R.string.detail_shopping_message_success)),
        FAIL(CalApplication.ApplicationContext().getString(R.string.detail_shopping_message_fail))
    }

    private val _stringResult = mutableStateOf<String>("")
    val stringResult: State<String> get() = _stringResult

    private var _messageState = mutableStateOf(false)
    val messageState: State<Boolean> = _messageState

    private var _loadingProgressState = mutableStateOf(false)
    val loadingProgressState: State<Boolean> = _loadingProgressState

    private var _updateResult = mutableStateOf(false)
    val updateResult: State<Boolean> = _updateResult


    fun updatePhoneNumber(documentId: String,
                          newPhone: String,
                          newAddress: String,
                          newAddressNumber: String
    ) {
        viewModelScope.launch {
            _loadingProgressState.value = true
            val result = repo.updateUserInfo(documentId,newPhone,newAddress, newAddressNumber)
            // 결과를 LiveData 또는 State로 설정합니다.
            // _updateResult.value = result

            // 여기서 결과를 처리합니다. (예: UI에 메시지 표시)
            when (result) {
                is RepoResult.Success -> {
                    // 성공 처리
                    _updateResult.value = true
                    setErrorMessage(ShoppingCartResult.SUCCESS.message)
                    println("Phone updated successfully")
                }
                is RepoResult.Error -> {
                    // 에러 처리
                    _updateResult.value = false
                    setErrorMessage(ShoppingCartResult.FAIL.message)
                    println("Error updating phone: ${result.exception.message}")
                }
            }
        }
    }

    private val supervisorJob = SupervisorJob()
    private suspend fun setErrorMessage(message: String) {
        _loadingProgressState.value = false
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
            registerRepo: RegisterRepository
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShoppingCartViewModel(registerRepo) as T
            }
        }
    }
}