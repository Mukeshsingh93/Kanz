package com.example.myapplication.confirmpassword

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.forgetpassword.ForgetPasswordViewModel

class ConfirmPasswordViewModelFactory (val sharedPreferences: SharedPreferences,val otp:String,
                                       private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmPasswordViewModel::class.java)) {
            return ConfirmPasswordViewModel(sharedPreferences,otp,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}