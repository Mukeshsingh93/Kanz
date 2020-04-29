package com.example.myapplication.forgetpassword

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
import com.example.myapplication.util.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ForgetPasswordViewModel (val sharedPreferences: SharedPreferences,
                               application: Application
) : AndroidViewModel(application)
{

    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _navigateToScreen = MutableLiveData<Int>()
    val navigateToScreen : LiveData<Int?>
        get() = _navigateToScreen

    private val _textValue = MutableLiveData<String>()
    val textValue : LiveData<String?>
        get() = _textValue

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _otp= MutableLiveData<String>()
    val otp : LiveData<String?>
        get() = _otp

    init {
        _navigateToScreen.value = 0
        _textValue.value = ""
        _otp.value=""

    }


    fun onTextChangedValue(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _textValue.value= s.toString()
        }
        else
        {
            _textValue.value=""
        }
    }

    fun complete()
    {
        _navigateToScreen.value = 0
        _message.value = null
    }

    fun submit()
    {
        when {
            _textValue.value!!.isEmpty() -> _message.value="The field is required."
          //  !ValidationUtil.isEmailValid(_textValue.value.toString()) ->_message.value="The Email address is not valid"
            else -> forgetPasswordApi()
        }
    }

    fun forgetPasswordApi()
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
                var getPropertiesDeferred = CarzApi.retrofitService.forgetPassword(textValue.value.toString())
                try {
                    val response = getPropertiesDeferred.await()
                    Log.e(Constant.APIRESPONSE,"forgetpassword api response is......"+response.toString())
                    _status.value = ApiStatus.DONE
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _message.value= response.message
                        _otp.value=response.datas!!.code
                        sharedPreferences.edit { putString(Constant.TOKEN,response.datas!!.token)}
                        _navigateToScreen.value = 1
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"forgetpassword api failure is......"+e.toString())
                }
            }
        }
    }



}