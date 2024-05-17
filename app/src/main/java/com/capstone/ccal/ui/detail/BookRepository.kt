package com.capstone.ccal.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookCategoryResponse
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem

/**
 * 개별 책 아이템 가져오기
 */
class BookRepository {
    private val _bookDetailRes = MutableLiveData<BookDetailItem>()
    val bookDetailRes: LiveData<BookDetailItem> get() = _bookDetailRes

    suspend fun requestBookDetail(bookId: String) {
        val detailRepo = BaseRepository(AppConst.FIREBASE.BOOK_DETAIL, BookDetailItem::class.java)

        when (val result = detailRepo.getDocumentsByField("bookId", bookId)) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getBookDetail RepoResult.Success")
                val data = result.data

                if (data.isNotEmpty()) {
                    _bookDetailRes.value = data[0]
                }
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> getFeed RepoResult.Error ${result.exception}")

            }
        }
    }

    suspend fun addSampleBook() {
        val detailRepo = BaseRepository(AppConst.FIREBASE.BOOK_DETAIL, BookDetailItem::class.java)

        when (val result = detailRepo.addDocument(BookDetailItem())) {
            is RepoResult.Success -> {
                Log.d("seki", ">>addSampleItem Success")
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> addSampleItem Error")
            }
        }
    }
}