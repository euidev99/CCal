package com.capstone.ccal.model


data class BookCategoryDto(
    val category: String = ""
)

data class BookCategoryResponse(
    val categoryList: List<BookCategoryDto>
)
