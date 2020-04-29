package com.example.myapplication.contactus

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

class ContactusViewModel (val sharedPreferences: SharedPreferences,
                          application: Application
) : AndroidViewModel(application)
{

    private val context = getApplication<Application>().applicationContext
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateRegister = MutableLiveData<Int>()
    val navigateRegister : LiveData<Int?>
        get() = _navigateRegister

    private val _fullname = MutableLiveData<String>()
    val fullname : LiveData<String?>
        get() = _fullname

    private val _email = MutableLiveData<String>()
    val email : LiveData<String?>
        get() = _email

    private val _phoneno = MutableLiveData<String>()
    val phoneno : LiveData<String?>
        get() = _phoneno

    private val _messageText = MutableLiveData<String>()
    val messageText : LiveData<String?>
        get() = _messageText

    private val _message= MutableLiveData<String>()
    val message : LiveData<String?>
        get() = _message

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _sessionEx = MutableLiveData<Int?>()
    val sessionEx: LiveData<Int?>
        get() = _sessionEx


    init {
        _navigateRegister.value = 0
        _fullname.value = ""
        _email.value = ""
        _phoneno.value = ""
        _messageText.value = ""
    }

    fun onTextChangedFullName(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _fullname.value= s.toString()
        }
        else
        {
            _fullname.value=""
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

    fun onTextChangedPhoneno(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _phoneno.value= s.toString()
        }
        else
        {
            _phoneno.value=""
        }
    }

    fun onTextChangedMessage(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!s.toString().isEmpty())
        {
            _messageText.value= s.toString()
        }
        else
        {
            _messageText.value=""
        }
    }


    fun complete()
    {
        _navigateRegister.value = 0
    }

    fun submit()
    {
        when {
            _fullname.value!!.isEmpty() -> _message.value="The full name is required."
            _email.value!!.isEmpty() -> _message.value="The email field is required."
            !ValidationUtil.isEmailValid(_email.value.toString()) ->_message.value="The Email address is not valid"
            _phoneno.value!!.isEmpty() -> _message.value="The phone no field is required."
            _messageText.value!!.isEmpty() -> _message.value="The message is required."
            else -> contactusApi()
        }
    }



    fun contactusApi()
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
                var getPropertiesDeferred = CarzApi.retrofitService.contactus(fullname.value.toString(),email.value.toString(),phoneno.value.toString(),messageText.toString())
                try {
                    val response = getPropertiesDeferred.await()

                    Log.e(Constant.APIRESPONSE,"contactus api response is......"+response.toString())

                    _status.value = ApiStatus.DONE
                    if(response.status== Constant.SUCCEESSSTATUS)
                    {
                        _message.value= response.message
                    }
                    else if(response.status== Constant.TOKENEXPIRED)
                    {
                        _message.value= response.message
                        _sessionEx.value = 5
                    }
                    else
                    {
                        _message.value= response.message
                    }
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value= "Api Failure "+e.message
                    Log.e(Constant.APIRESPONSE,"contactus api failure is......"+e.toString())
                }
            }
        }
    }

    fun register()
    {
        _navigateRegister.value = 1
    }

}