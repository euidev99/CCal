package com.capstone.ccal.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.ccal.model.BookCategoryResponse
import com.capstone.ccal.model.BookCollectionResponse
import kotlinx.coroutines.launch

class FeedViewModel(
    private val feedRepo: FeedRepo
) : ViewModel() {

    val feedRes: LiveData<BookCollectionResponse> get() = feedRepo.feedResponse
    val categoryRes: LiveData<BookCategoryResponse> get() = feedRepo.categoryResponse

    fun fetchData() {
        viewModelScope.launch {
            Log.d("seki", ">> Feed ViewModel fetchData")
            feedRepo.requestBookCategory()
            feedRepo.requestFeedCollection()
//            feedRepo.addSampleCollection()
//            feedRepo.addSampleCategory()
        }
    }


    companion object {
        fun provideFactory(
            feedRepository: FeedRepo
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(feedRepository) as T
            }
        }
    }
}