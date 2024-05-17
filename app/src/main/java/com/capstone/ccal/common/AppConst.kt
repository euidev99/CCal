package com.capstone.ccal.common

interface AppConst {

    interface KEY {
        companion object {
            const val SHOW_ID = "Show_id"
            const val WEB_URL = "Web_url"
            const val USER_ID = "User_id"
            const val USER_NAME = "User_name"
            const val USER_PASSWORD = "UserPassword"
            const val MY_PREFERENCE = "MyPreferences"
        }
    }

    interface FIREBASE {
        companion object {

            const val FEED_COLLECTION = "Feed"
            const val BOOK_DETAIL = "BookDetail"
            const val BOOK_CATEGORY = "Category"
            const val USER_INFO = "UserInfo"

        }
    }

}