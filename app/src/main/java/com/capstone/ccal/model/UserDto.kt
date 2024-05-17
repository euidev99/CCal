package com.capstone.ccal.model


/**
* 이메일 형식으로 가입하도록 변경
*/
data class UserDto(
    val email: String = "baseEmail@email.com", // 아이디
    val name: String = "", //이름이나 닉네임
    val password: String = "", //비밀번호
    //val categoryList: String = "",// ArrayList<String> = arrayListOf() //관심분야
    val image: String = ""
)