package com.capstone.ccal.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.FirebaseStorageUtils
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.AuthorDto
import com.capstone.ccal.model.BookCategoryResponse
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

                    val firebaseImageUrl = FirebaseStorageUtils.getImageUrl(data[0].bookImageUrl)

                    val updatedList = mutableListOf<String>()
                    // 기존의 bookImageList를 순회하면서 Firebase로부터 이미지 URL을 가져와서 업데이트
                    for (imageUrl in data[0].bookImageList) {
                        val firebaseUrl = FirebaseStorageUtils.getImageUrl(imageUrl)
                        updatedList.add(firebaseUrl)
                    }

                    val updatedAuthorList = mutableListOf<AuthorDto>()
                    for (author in data[0].authorList) {
                        val firebaseUrl = FirebaseStorageUtils.getImageUrl(author.imageUrl)

                        updatedAuthorList.add(author.copy(imageUrl = firebaseUrl))
                    }

                    _bookDetailRes.value = data[0].copy(
                        bookImageUrl = firebaseImageUrl,
                        bookImageList = updatedList,
                        authorList = updatedAuthorList
                    )
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