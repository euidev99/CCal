package com.capstone.ccal.ui.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.model.BookItemDto
import com.capstone.ccal.model.BookList
import com.capstone.ccal.model.BookSearchResponse
import com.capstone.ccal.model.BookTypeCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Detail 목록 안에서 찾아오면 될듯
 * 혹은 그냥 데이터 하나 더 파고
 */
class SearchRepo {
    enum class SearchResult(val message: String) {
        SUCCESS(CalApplication.ApplicationContext().getString(R.string.search_message_success)),
        FAILED(CalApplication.ApplicationContext().getString(R.string.search_message_fail)),
        NO_RESULT(CalApplication.ApplicationContext().getString(R.string.search_message_no_result)),
        ERROR_COMMON(CalApplication.ApplicationContext().getString(R.string.search_message_error))
    }

    private var _loadingProgressState = mutableStateOf(false)
    val loadingProgressState: State<Boolean> = _loadingProgressState

    val _stringMessage = mutableStateOf<String>("")
    val stringMessage: State<String> get() = _stringMessage

    private var _messageState = mutableStateOf(false)
    val messageState: State<Boolean> = _messageState

    private val _bookListResponse = MutableLiveData<BookSearchResponse>()
    val bookListResponse: LiveData<BookSearchResponse> get() = _bookListResponse

    suspend fun requestDetailCollection() {
        _loadingProgressState.value = true

        val searchRepository =
            BaseRepository(AppConst.FIREBASE.BOOK_DETAIL, BookDetailItem::class.java)

        when (val result = searchRepository.getAllDocuments()) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getSearch RepoResult.Success")
                val data = BookSearchResponse(result.data)

                _bookListResponse.value = data
                _loadingProgressState.value = false
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> getSearch RepoResult.Error ${result.exception}")


                _loadingProgressState.value = false
            }
        }
    }

    suspend fun requestSearchDetailByName(keyword: String) {
        _loadingProgressState.value = true

        val searchRepository =
            BaseRepository(AppConst.FIREBASE.BOOK_DETAIL, BookDetailItem::class.java)

        when (val result = searchRepository.getDocumentsByField("bookName", keyword)) {
            is RepoResult.Success -> {
                Log.d("seki", ">>get Search RepoResult.Success: keyword : $keyword")

                val data = BookSearchResponse(result.data)
                _bookListResponse.value = data
                setErrorMessage(SearchResult.SUCCESS.message)
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> getSearch RepoResult.Error ${result.exception}")
                setErrorMessage(SearchResult.ERROR_COMMON.message)
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
            _stringMessage.value = message
            delay(3000)
            _messageState.value = false
            _stringMessage.value = ""
        }
    }

}