package com.capstone.ccal.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.ccal.model.BookCollectionResponse
import com.capstone.ccal.model.BookDetailItem
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: BookRepository
) : ViewModel() {

    val feedRes: LiveData<BookDetailItem> get() = repo.bookDetailRes

    fun fetchData(bookId: String) {
        viewModelScope.launch {
            Log.d("seki", ">> detailViewModel fetchData")
            repo.requestBookDetail(bookId)
//            repo.addSampleBook()
        }
    }

    companion object {
        fun provideFactory(
            bookRepository: BookRepository
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(bookRepository) as T
            }
        }
    }
}