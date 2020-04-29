package net.simplifiedcoding


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ProfileTabviewBinding
import com.example.myapplication.util.Constant
import com.example.myapplication.util.HomeViewActivity
import com.example.myapplication.util.MainActivity
import kotlinx.android.synthetic.main.profile_tabview.view.*


class ProfileTab : Fragment() {

    lateinit var binding: ProfileTabviewBinding
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)

        binding = ProfileTabviewBinding.inflate(inflater)
        var view = inflater.inflate(R.layout.profile_tabview, container, false)

        val logout = view.findViewById(R.id.logout) as TextView

        var token =sharedPreferences.getString(Constant.TOKEN,"")
        Log.e("LOGOUTOUT","logout value......."+token.toString())

        if(token.equals(""))
        {
            Log.e("LOGOUTOUT","logout value..login....."+token.toString())
           // view.logout.text= "login"
           binding.logout.text= (activity as AppCompatActivity).resources.getString(R.string.login)
        }
        else
        {
            Log.e("LOGOUTOUT","logout value...logout...."+token.toString())
            binding.logout.text=resources.getString(R.string.logout)
        }

        binding.profilelayout.setOnClickListener {
            var token =sharedPreferences.getString(Constant.TOKEN,"")
            if(token.equals(""))
            {
                (activity as HomeViewActivity).logOut()
            }
            else {
                val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.PROFILEVIEW)
                startActivity(intent)
            }
        }

        binding.mywhishlistlayout.setOnClickListener {

            var token =sharedPreferences.getString(Constant.TOKEN,"")
            if(token.equals(""))
            {
                (activity as HomeViewActivity).logOut()
            }
            else {
                val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.WISHLISTVIEW)
                startActivity(intent)
            }

        }

        binding.orderlayout.setOnClickListener {

            var token =sharedPreferences.getString(Constant.TOKEN,"")
            if(token.equals(""))
            {
                (activity as HomeViewActivity).logOut()
            }
            else {
                val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
                intent.putExtra("viewType", Constant.ORDERVIEW)
                startActivity(intent)
            }

        }

        binding.languagelayout.setOnClickListener {

            (activity as HomeViewActivity).showDialog()
        }

        binding.contactuslayout.setOnClickListener {

            val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
            intent.putExtra("viewType", Constant.CONTACTUS)
            startActivity(intent)
        }

        binding.howitworkslayout.setOnClickListener {

            val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
            intent.putExtra("viewType", Constant.WEBVIEW)
            startActivity(intent)
        }

        binding.faqlayout.setOnClickListener {

            val intent = Intent(activity as AppCompatActivity, MainActivity::class.java)
            intent.putExtra("viewType", Constant.FAQ)
            startActivity(intent)
        }

        binding.logoutlayout.setOnClickListener {


            (activity as HomeViewActivity).logOut()

           /* AlertDialog.Builder(activity as AppCompatActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->

                        (activity as HomeViewActivity).logOut()

                      //  sharedPreferences.edit().clear().apply()
                        //    (activity as AppCompatActivity).finish()
//                        val intent = Intent(activity as AppCompatActivity, Activity_Login::class.java)
//                        startActivity(intent)
//                        (activity as AppCompatActivity).finish()
                    })
                .setNegativeButton("No", null)
                .show()*/
        }

        return binding.root
    }

}

