package com.capstone.ccal.ui.mypage

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.ccal.model.BookCategoryResponse
import com.capstone.ccal.model.BookCollectionResponse
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val myPageRepo: MyPageRepo
) : ViewModel() {

//    val feedRes: LiveData<BookCollectionResponse> get() = myPageRepo.feedResponse
//    val categoryRes: LiveData<BookCategoryResponse> get() = myPageRepo.categoryResponse


    var myCartState = mutableStateOf(false)
        private set

    var myAppInfoState = mutableStateOf(false)
        private set

    fun updateMyCartState(state: Boolean) {
        myCartState.value = state
    }

    fun updateMyAppInfoState(state: Boolean) {
        myAppInfoState.value = state
    }

    companion object {
        fun provideFactory(
            myPageRepo: MyPageRepo
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyPageViewModel(myPageRepo) as T
            }
        }
    }
}