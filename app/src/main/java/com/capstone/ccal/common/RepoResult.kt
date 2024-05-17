package com.capstone.ccal.common

/**
 * Result CallBack 을 사용할 것이나, Base 타입을 지정할 때,
 * DTO 혹은 DAO Class 를 지정하기 위하여 제너릭 타입을 사용
 */
sealed class RepoResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : RepoResult<T>()
    data class Error(val exception: Exception) : RepoResult<Nothing>()
}