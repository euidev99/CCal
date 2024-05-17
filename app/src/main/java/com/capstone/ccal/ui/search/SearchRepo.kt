package com.capstone.ccal.ui.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookItemDto
import com.capstone.ccal.model.BookList
import com.capstone.ccal.model.BookTypeCollection

/**
 * Feed 안에서 찾아오기?
 * 혹은 전체 안에서 찾아오기 고민
 */
class SearchRepo {
    private val _feedResponse = MutableLiveData<BookCollectionResponse>()
    val feedResponse: LiveData<BookCollectionResponse> get() = _feedResponse

    private val _bookResponse = MutableLiveData<BookList>()
    val bookResponse : LiveData<BookList> get() = _bookResponse

    suspend fun requestFeedCollection() {
        val feedRepository =
            BaseRepository(AppConst.FIREBASE.FEED_COLLECTION, BookCollectionResponse::class.java)

        when (val result = feedRepository.getAllDocuments()) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getFeed RepoResult.Success")
                val data = result.data
                _feedResponse.value = data[0]
                // Feed Collection 이 여러개는 아니니까 .. Firebase Tree 라서 혼동
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> getFeed RepoResult.Error ${result.exception}")

            }
        }
    }

    suspend fun requestBookFromTitle(fieldName: String, value: String) {
        val feedRepository =
            BaseRepository(AppConst.FIREBASE.FEED_COLLECTION, BookItemDto::class.java)

        when (val result = feedRepository.getDocumentsByField(fieldName = fieldName, value = value)) {
            is RepoResult.Success -> {
                Log.d("seki", ">> Success")
                val data = result.data
                _bookResponse.value = BookList(data) // 그냥 list인데, mutableLiveData 는, list 타입으로 받는걸 권장하지 않음
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> Fail")
            }

            else -> {}
        }
    }
}