package com.capstone.ccal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val intent = Intent(this, UnityPlayerActivity::class.java)
//            this.startActivity(intent)
            CalCalCalApp()
        }
    }
}