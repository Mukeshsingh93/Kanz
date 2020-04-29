package com.example.myapplication.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class Login : Fragment()
{
    private lateinit var vi: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vi = inflater.inflate(R.layout.fragment_login,container,false)
        setHasOptionsMenu(true)
        return vi
    }
}