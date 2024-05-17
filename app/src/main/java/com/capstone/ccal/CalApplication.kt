package com.capstone.ccal

import android.app.Application
import android.content.Context

class CalApplication : Application() {

    init{
        instance = this
    }

    companion object {
        lateinit var instance: CalApplication
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }

}