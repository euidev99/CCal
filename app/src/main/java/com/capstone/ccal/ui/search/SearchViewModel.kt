package com.capstone.ccal.ui.search

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


class SearchViewModel(
    private val searchRepo: SearchRepo
): ViewModel() {

    val searchResult get() = searchRepo.bookListResponse
    val loadingProgressState: State<Boolean> get() = searchRepo.loadingProgressState
    val errorMessageOn: State<Boolean> get() = searchRepo.messageState
    val messageResult: State<String> get() = searchRepo.stringMessage


    //이메일 등록
//    fun setEmailAndPassword(email: String, password: String) {
//        Log.d("seki", "emailInput : $email, password : $password")
//        _uiState.update { currentState ->
//            currentState.copy(
//                email = email,
//                password = password
//            )
//        }
//    }
//
//    //닉네임, 프로필 이미지 등록imageUri: Uri,
//    fun setProfile(nickName: String) {
//        _uiState.update { currentState ->
//            currentState.copy(
////                profileImageUri = imageUri,
//                nickname = nickName
//            )
//        }
//    }
//
//    //이메일 중복 체크
//    fun checkDuplicated(email: String) {
//        viewModelScope.launch {
//            registerRepo.checkDuplicated(email)
//        }
//    }

    //검색
    fun requestWithKeyword(keyword: String) {
        viewModelScope.launch {
            searchRepo.requestSearchDetailByName(
                keyword = keyword
            )
        }
    }

    companion object {
        fun provideFactory(
            searchRepo: SearchRepo
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(searchRepo) as T
            }
        }
    }
}
