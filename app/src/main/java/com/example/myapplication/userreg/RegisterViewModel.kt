package com.example.myapplication.userreg

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
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

class RegisterViewModel (val sharedPreferences: SharedPreferences,
                         application: Application
) : AndroidViewModel(application)
{


    private val context = getApplication<Application>().applicationContext

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _email = MutableLiveData<String>()
    val email : LiveData<String?>
        get() = _email


    private val _password = MutableLiveData<String>()
    val password : LiveData<String?>
        get() = _password

    private val _username = MutableLiveData<String>()
    val username : LiveData<String?>
        get() = _username

    private val _mobileno = MutableLiveData<String>()
    val mobileno : LiveData<String?>
        get() = _mobileno

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _otp= MutableLiveData<String>()
    val otp : LiveData<String?>
        get() = _otp

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _navigateVerification = MutableLiveData<Int>()
    val navigateVerification : LiveData<Int?>
        get() = _navigateVerification

    private val _termCondition= MutableLiveData<Boolean>()
    val termCondition: LiveData<Boolean>
        get() = _termCondition

    init {
        _username.value=""
        _otp.value=""
        _mobileno.value=""
        _email.value=""
        _password.value=""
        _navigateVerification.value=0
        _termCondition.value=false

    }

    fun setTermCondition(value:Boolean)
    {
        _termCondition.value = value
    }



    fun complete()
    {
        _navigateVerification.value = 0
    }

    fun onTextChangedUsername(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _username.value= s.toString()
        }
        else
        {
            _username.value=""
        }
    }

    fun onTextChangedMobileno(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _mobileno.value= s.toString()
        }
        else
        {
            _mobileno.value=""
        }
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

    fun onTextChangedPassword(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _password.value= s.toString()
        }
        else
        {
            _password.value=""
        }
    }

    fun register()
    {
     // _navigateVerification.value=1

        when {
            _username.value!!.isEmpty() -> _message.value="The name field is required."
            _mobileno.value!!.isEmpty() -> _message.value="The mobile no field is required."
            _email.value!!.isEmpty() -> _message.value="The email field is required."
            !ValidationUtil.isEmailValid(_email.value.toString()) ->_message.value="The Email address is not valid"
            _password.value!!.isEmpty() -> _message.value="The password field is required."
            _password.value!!.length<6 -> _message.value="The password length is less than is required."
            !termCondition.value!! -> _message.value="In order to proceed, you need to accept our Terms and Conditions"
         else -> regFirstStepApi()
        }
    }


    fun regFirstStepApi()
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
                Log.e("RESPONSE","email...."+email.value.toString()+"...pin...")
                var getPropertiesDeferred = CarzApi.retrofitService.register(username.value.toString(),email.value.toString(),mobileno.value.toString(),password.value.toString(),"android","eyJ0eXAiOiJKV1QiLCJh")
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"registration api response is......"+response.toString())

                    _status.value = ApiStatus.DONE
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                       // _message.value= response.message
                        _otp.value=response.otp_code
                        _navigateVerification.value=1
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"registration api failure is......"+e.toString())
                }
            }
        }
    }
}