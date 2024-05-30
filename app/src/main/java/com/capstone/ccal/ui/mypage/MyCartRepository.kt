package com.capstone.ccal.ui.mypage

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.ccal.CalApplication
import com.capstone.ccal.CalApplication.Companion.ApplicationContext
import com.capstone.ccal.R
import com.capstone.ccal.common.AppConst
import com.capstone.ccal.common.BaseRepository
import com.capstone.ccal.common.RepoResult
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.model.Order
import com.capstone.ccal.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * 조작이 필요 없으니 뷰모델로 이동
 */
class MyCartRepository {
    suspend fun getMyOrder(email: String): RepoResult<List<Order>> {
        val orderRepository = BaseRepository(AppConst.FIREBASE.ORDER, Order::class.java)

        return orderRepository.getAllDocumentsFromSecondCollection(email, AppConst.FIREBASE.ORDER)
    }
}