package com.capstone.ccal.ui.mypage

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
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.common.UserRepository
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.model.BookSearchResponse
import com.capstone.ccal.model.Order
import com.capstone.ccal.model.OrderListResponse
import com.capstone.ccal.ui.login.RegisterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyCartViewModel(
    private val repo: MyCartRepository
) : ViewModel() {

    private val _stringResult = mutableStateOf<String>("")
    val stringResult: State<String> get() = _stringResult

    private var _messageState = mutableStateOf(false)
    val messageState: State<Boolean> = _messageState

    private var _loadingProgressState = mutableStateOf(false)
    val loadingProgressState: State<Boolean> = _loadingProgressState

    private val _orderListResponse = MutableLiveData<OrderListResponse>()
    val orderListResponse: LiveData<OrderListResponse> get() = _orderListResponse

    fun getMyOrder() {
        val email = UserRepository.getEmail(context = CalApplication.ApplicationContext())

        viewModelScope.launch {
            _loadingProgressState.value = true
            val result = repo.getMyOrder(email)

            when (result) {
                is RepoResult.Success -> {
                    _orderListResponse.value = OrderListResponse(orderList = result.data)
                    setErrorMessage(message = "로드 완료")
                }
                is RepoResult.Error -> {
                    _loadingProgressState.value = false
                    setErrorMessage(message = "에러가 발생하였습니다. 잠시 후 시도해주세요.")
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
            myCartRepo: MyCartRepository
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyCartViewModel(myCartRepo) as T
            }
        }
    }
}