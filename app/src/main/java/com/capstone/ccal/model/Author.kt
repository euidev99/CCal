package com.capstone.ccal.model


/**
 * 작가 상세 정보
 */
data class AuthorDto(
    val authorId: String = "작가Id",
    val authorName: String = "작가명",
    val description: String = "작가님 설명",
    val authorType: String = "작가" //역자 식자 등등
)

/**
 * 작가 정보 ResponseData
 */
data class AuthorCollectionResponse(
    val collectionList: List<AuthorDto> = listOf()
)

data class AuthorBookDto(
    val collectionList: List<BookItemDto> = listOf()
)