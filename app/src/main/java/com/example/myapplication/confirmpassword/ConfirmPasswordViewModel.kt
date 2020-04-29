package com.example.myapplication.confirmpassword

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConfirmPasswordViewModel (val sharedPreferences: SharedPreferences,val otp:String,
                                application: Application
) : AndroidViewModel(application)
{

    private val context = getApplication<Application>().applicationContext
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _otpvalue = MutableLiveData<String>()
    val otpvalue : LiveData<String?>
        get() = _otpvalue

    private val _newPassword = MutableLiveData<String>()
    val newPassword : LiveData<String?>
        get() = _newPassword

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword : LiveData<String?>
        get() = _confirmPassword


    private val _navigateToScreen = MutableLiveData<Int>()
    val navigateToScreen : LiveData<Int?>
        get() = _navigateToScreen



    init {
        _otpvalue.value=""
        _newPassword.value=""
        _confirmPassword.value=""
        _navigateToScreen.value = 0
    }

    fun onTextChangedOtp(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _otpvalue.value= s.toString()
        }
        else
        {
            _otpvalue.value=""
        }
    }

    fun onTextChangedNewPassword(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _newPassword.value= s.toString()
        }
        else
        {
            _newPassword.value=""
        }
    }

    fun onTextChangedConfirmPassword(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _confirmPassword.value= s.toString()
        }
        else
        {
            _confirmPassword.value=""
        }
    }



    fun complete()
    {
        _navigateToScreen.value = 0
        _message.value = null
    }

fun submit()
    {
    val matchotp : Boolean =(otp == otpvalue.value)
    val matchPassword : Boolean =(newPassword.value == confirmPassword.value)
        when {
        _otpvalue.value!!.isEmpty() -> _message.value="The code field is required."
        _newPassword.value!!.isEmpty() -> _message.value="The new password field is required."
        _newPassword.value!!.length<6 -> _message.value="The password is minimum 6 character is required."
        _confirmPassword.value!!.isEmpty() -> _message.value="The confirm password field is required."
        !matchPassword ->  _message.value="Confirm Password does not match"
        !matchotp ->  _message.value="You have entered wrong otp"
        else -> changepasswordapi()
    }
   }

    fun changepasswordapi()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            _status.value = ApiStatus.LOADING
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                var getPropertiesDeferred = CarzApi.retrofitService.changePassword("bearer "+token,newPassword.value.toString())
                try {
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"changepassword api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _message.value= response.message
                        sharedPreferences.edit { putString(Constant.TOKEN,"")}
                        _navigateToScreen.value = 1

                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"changepassword api failure is......"+e.toString())

                }
            }
        }
    }

    fun register()
    {
    }

}