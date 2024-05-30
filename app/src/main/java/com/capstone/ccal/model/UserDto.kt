package com.capstone.ccal.model


/**
* 이메일 형식으로 가입하도록 변경
*/
data class UserDto(
    val email: String = "baseEmail@email.com", // 아이디
    val name: String = "", //이름이나 닉네임
    val password: String = "", //비밀번호
    //val categoryList: String = "",// ArrayList<String> = arrayListOf() //관심분야
    val image: String = "",
    val phoneNum: String = "",
    val address: String = "",
    val addressNumber: String = "",
    val deliveryMemo: String = ""
)

/**
 * 주문내역
 */
data class Order(
    val orderDate: String = "",
    val phone: String = "",
    val userName: String = "",
    val itemName: String = "",
    val itemPoint: String = "",
    val itemId: String = "",
    val completion: String = DeliveryState.WAITING.message,
)

data class OrderListResponse(
    val orderList: List<Order>
)


enum class DeliveryState(val message: String ) {
    WAITING("배송 준비"),
    COMPLETE("배송 완료"),
}