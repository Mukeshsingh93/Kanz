package com.example.myapplication.loginview

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.CarzApi
import com.example.myapplication.util.Constant
import com.example.myapplication.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel (val sharedPreferences: SharedPreferences,
                      application: Application
) : AndroidViewModel(application)
{

    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)




    private val _navigateToScreen = MutableLiveData<Int>()
    val navigateToScreen : LiveData<Int?>
        get() = _navigateToScreen



    private val _email = MutableLiveData<String>()
    val email : LiveData<String?>
        get() = _email


    private val _pin = MutableLiveData<String>()
    val pin : LiveData<String?>
        get() = _pin

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status


    init {
        _navigateToScreen.value = 0
        _email.value = ""
        _pin.value = ""
    }

    fun onTextChangedEmail(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _email.value= s.toString()
        }
        else
        {
            _email.value=""
        }
   }

fun onTextChangedPin(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _pin.value= s.toString()
        }
        else
        {
            _pin.value=""
        }
    }


    fun complete()
    {
        _navigateToScreen.value = 0
    }

    fun signin()
    {
        when {
               _email.value!!.isEmpty() -> _message.value="The email field is required."
              !ValidationUtil.isEmailValid(_email.value.toString()) ->_message.value="The Email address is not valid"
              _pin.value!!.isEmpty() -> _message.value="The pin field is required."
              else -> loginApi()
        }
    }

    fun loginApi()
    {
        if(!Constant.connected(context))
        {
            _message.value= context.resources.getString(R.string.nointernet)
        }
        else
        {
            _status.value = ApiStatus.LOADING
            coroutineScope.launch {
                // Get the Deferred object for our Retrofit request
                Log.e("RESPONSE","email...."+email.value.toString()+"...pin..."+pin.value.toString())
                var getPropertiesDeferred = CarzApi.retrofitService.login(email.value.toString(),pin.value.toString(),"android","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjYsImlzcyI6Imh0dHBzOi8va2Fuei5hcHAvZGVtby9hcGkvdXNlci9sb2dpbiIsImlhdCI6MTU4MTc3MDM4NiwiZXhwIjoxNTgxNzczOTg2LCJuYmYiOjE1ODE3NzAzODYsImp0aSI6ImV5Q0k1aFdsdlQ3bmt0RWQifQ.7Yjxz0ufupXy14YflGzYAZil0Fc6yXTcyQmc3alBSRg")
                try {
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"login api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status==Constant.SUCCEESSSTATUS)
                    {
                     //   _message.value= response.message
                        sharedPreferences.edit { putString(Constant.TOKEN,response.token)}
                        _navigateToScreen.value=3
                    }
                    else
                    {
                        _message.value= response.message
                    }

                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"login api failure is......"+e.toString())
               }
            }
        }
    }

    fun register()
    {
        _navigateToScreen.value = 1
    }

    fun forgetPassword()
    {
        _navigateToScreen.value = 2
    }

}