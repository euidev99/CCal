package com.capstone.ccal.model

/**
 * 책 메인 컬렉션
 */
data class BookTypeCollection(
    val collectionType: Int = 0,
    val collectionName: String = "",
    val mainCategory: String = "",
    val itemList: List<BookItemDto> = listOf()
)

/**
 * Feed 에 표기할 책 목록 데이터
 */
data class BookItemDto(
    val bookId: String = "bookId",
    val bookName: String = "bookName",
    val authorList: List<AuthorDto> = listOf(AuthorDto(), AuthorDto()),
    val price: Int = 0,
    val contentLevel: Int = 0,
    val mainCategory: String = "",
    val imageUrl: String = ""
)

/**
 * 전체 Book Collections
 */
data class BookCollections(
    val collectionList: List<BookTypeCollection>
)

data class BookCollectionResponse(
    val collectionList: List<BookTypeCollection> = listOf()
)

/**
 * 책 목록
 * Search 화면 등에서 사용
 */
data class BookList(
    val bookList: List<BookItemDto>
)

/**
 * 책 디테일 화면에 쓸 데이터
 */
data class BookDetailItem(
    val bookId: String = "bookId",
    val bookName: String = "bookName",
    val bookImageUrl: String = "",
    val authorList: List<AuthorDto> = listOf(AuthorDto(), AuthorDto()),
    val price: Int = 0,
    val contentLevel: Int = 0,
    val mainCategory: String = "",
    val genreList: List<String> = listOf("동화", "교훈", "서스펜스"),
    val bookDescription: String = "책 설명",
)
