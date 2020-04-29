package com.example.myapplication

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class Activity_Login : AppCompatActivity()
{
    lateinit var test: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        test="lelo"
        Log.e("FINALDATA","value is  getAvailableMemory().."+getAvailableMemory().availMem)
    }

    // Get a MemoryInfo object for the device's current memory status.
    private fun getAvailableMemory(): ActivityManager.MemoryInfo {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return ActivityManager.MemoryInfo().also { memoryInfo ->
            activityManager.getMemoryInfo(memoryInfo)
        }
    }

}