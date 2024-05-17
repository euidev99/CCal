package com.capstone.ccal.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookCategoryDto
import com.capstone.ccal.model.BookCategoryResponse
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookCollections
import com.capstone.ccal.model.BookItemDto
import com.capstone.ccal.model.BookTypeCollection

class FeedRepo {
    private val _feedResponse = MutableLiveData<BookCollectionResponse>()
    val feedResponse: LiveData<BookCollectionResponse> get() = _feedResponse

    private val _categoryResponse = MutableLiveData<BookCategoryResponse>()
    val categoryResponse: LiveData<BookCategoryResponse> get() = _categoryResponse

    suspend fun requestFeedCollection() {
        val feedRepository =
            BaseRepository(AppConst.FIREBASE.FEED_COLLECTION, BookTypeCollection::class.java)

        when (val result = feedRepository.getAllDocuments()) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getFeed RepoResult.Success")
                val data = result.data
                val responseList = BookCollectionResponse(data)

                _feedResponse.value = responseList
            }

            is RepoResult.Error -> {
                Log.d("seki", ">> getFeed RepoResult.Error ${result.exception}")

            }
        }
    }

    //책 카테고리
    suspend fun requestBookCategory() {
        val categoryRepo = BaseRepository(AppConst.FIREBASE.BOOK_CATEGORY, BookCategoryDto::class.java)

        when (val result = categoryRepo.getAllDocuments()) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getBookCategories.Success")
                _categoryResponse.value = BookCategoryResponse(result.data)
            }

            is RepoResult.Error -> {
                Log.d("seki", "getBookCategories.Error ${result.exception}")
            }
        }
    }

    suspend fun addSampleCategory() {
        val categoryRepo = BaseRepository(AppConst.FIREBASE.BOOK_CATEGORY, BookCategoryDto::class.java)

        val sampleCategory: BookCategoryDto = BookCategoryDto(category = "교육")
        val sampleCategory1: BookCategoryDto = BookCategoryDto(category = "그림책")
        val sampleCategory2: BookCategoryDto = BookCategoryDto(category = "일지")
        val sampleCategory3: BookCategoryDto = BookCategoryDto(category = "게임")
        val sampleCategory4: BookCategoryDto = BookCategoryDto(category = "sampleCategory2")
        val sampleCategory5: BookCategoryDto = BookCategoryDto(category = "sampleCategory3")
        val sampleCategory6: BookCategoryDto = BookCategoryDto(category = "sampleCategory4")
        val sampleResponse: BookCategoryResponse = BookCategoryResponse(
            listOf(
                sampleCategory1,sampleCategory2,sampleCategory3,sampleCategory4,sampleCategory5,sampleCategory6,
            ))

        when (val result = categoryRepo.addDocument(sampleCategory1)) {
            is RepoResult.Success -> {
                Log.d("seki", ">> getBookCategories.Success")
            }

            is RepoResult.Error -> {
                Log.d("seki", "getBookCategories.Error ${result.exception}")
            }
        }
    }

    suspend fun addSampleCollection() {
        val repository = BaseRepository(AppConst.FIREBASE.FEED_COLLECTION, BookTypeCollection::class.java)
//            BookCollectionResponse::class.java)

        val sampleBookItem = BookItemDto()
        val sampleList: List<BookItemDto> =
            listOf(sampleBookItem
                ,sampleBookItem
                ,sampleBookItem,
                sampleBookItem,
                sampleBookItem,
                sampleBookItem,
                sampleBookItem,
                sampleBookItem,
                sampleBookItem,
                sampleBookItem)
        val sampleCollection = BookTypeCollection(
            collectionType = 0,
            collectionName = "sample 4",
            itemList = sampleList
        )
        val bookCollectionResponse: BookCollectionResponse =
            BookCollectionResponse(listOf<BookTypeCollection>(sampleCollection, sampleCollection))


        when (val result = repository.addDocument(sampleCollection)) {
            is RepoResult.Success -> {

            }

            is RepoResult.Error -> {

            }
        }
    }

}