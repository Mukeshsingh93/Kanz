package com.example.myapplication.forgetpassword

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.loginview.LoginViewModel

class ForgetPasswordViewModelFactory (val sharedPreferences: SharedPreferences,
                                      private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            return ForgetPasswordViewModel(sharedPreferences,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}