package com.example.myapplication.profile

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ProductviewBinding
import com.example.myapplication.databinding.ProfileviewBinding
import com.example.myapplication.userreg.RegisterViewDirections
import com.example.myapplication.util.ApiStatus
import com.example.myapplication.util.MainActivity
import com.example.myapplication.util.ProgressDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import droidninja.filepicker.FilePickerBuilder
import kotlinx.android.synthetic.main.camera_dialog.view.*
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.profileview.*
import java.io.File
import java.util.ArrayList

class ProfileView  : Fragment()
{

    lateinit var binding: ProfileviewBinding
    private lateinit var viewModel : ProfileViewModel
    private lateinit var vi: View
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loader: ProgressDialog
    private val PERMISSION_REQUEST_CODE = 200
    private val FILE = 2
    val filepath = ArrayList<String>()//Creating an empty arraylist
    private val GALLERY = 1
    private val IMAGE_RESULT = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   vi = inflater.inflate(R.layout.fragment_registration_first,container,false)
        binding = DataBindingUtil.inflate<ProfileviewBinding>(
            inflater, R.layout.profileview, container, false)
        sharedPreferences =
            (activity as AppCompatActivity).getSharedPreferences("dd", Context.MODE_PRIVATE)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ProfileViewModelFactory(sharedPreferences, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
              //  Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                /*val snackbar = Snackbar
                    .make(profiletoplayout, it.toString(), Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.snackbarcolor))
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()*/

                toast(it)
            }
        })

        viewModel.navigateVerification.observe(viewLifecycleOwner, Observer {
            Log.e("SECONDREG","regist api is called....")
            it?.let {
                if(it==1)
                {
                    Log.e("CHECKDATA","ondata is called")
                    this.findNavController().navigate(
                        RegisterViewDirections.actionRegisterViewToVerificationView(viewModel.otp.value.toString()))
                    viewModel.complete()
                }
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.LOADING -> {
                    loader.setLoading(true)
                }
                ApiStatus.ERROR -> {
                    loader.setLoading(false)
                }
                ApiStatus.DONE -> {
                    loader.setLoading(false)
                }
            }
        })

        viewModel.notifyView.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it==1)
                {
                    binding.viewModel = viewModel
                }
            }
        })

        viewModel.sessionEx.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            it?.let {
                if (it == 5) {
                    (activity as MainActivity).logOut()
                    //   Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.backarrow.setOnClickListener {
            //  Toast.makeText(activity as AppCompatActivity,"Hello",Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).onBackPressed()

        }

        binding.viewModel = viewModel
        return  binding.root
    }

    private fun showPictureDialog() {
        if (checkPermission()) {
            showGalleryDialog()
            //  Snackbar.make(reg_third, "Permission already granted.", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(profiletoplayout, "Please request permission.", Snackbar.LENGTH_LONG).show()
            requestPermission()
        }
    }

    fun showGalleryDialog()
    {
        val dialogView = layoutInflater.inflate(R.layout.camera_dialog, null)
        val dialog = BottomSheetDialog((activity as AppCompatActivity))
        dialog.setContentView(dialogView)
        dialog.show()

        dialogView.fileBtn.setOnClickListener {
            dialog.hide()
            takeFile()
        }

        dialogView.galleryBtn.setOnClickListener {
            dialog.hide()
            chooseImageFromGallery()
        }

        dialogView.cameraBtn.setOnClickListener {
            dialog.hide()
            startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT)
        }
    }

    fun chooseImageFromGallery() {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, GALLERY)

        /*  FilePickerBuilder.instance.setMaxCount(5)
              .setSelectedFiles(filepath)
              .setActivityTheme(R.style.LibAppTheme)
              .pickPhoto(this);*/

        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(filepath)
            .setActivityTheme(R.style.LibApp)
            .pickPhoto(this,GALLERY);

//         Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, 7);
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            // Filter to only show results that can be "opened", such as a
//            // file (as opposed to a list of contacts or timezones)
//            addCategory(Intent.CATEGORY_OPENABLE)
//            // Filter to show only images, using the image MIME data type.
//            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
//            // To search for all documents available via installed storage providers,
//            // it would be "*/*".
//            type = "*/*"
//        }

        //   startActivityForResult(intent, GALLERY)
    }

    fun getPickImageChooserIntent(): Intent {

        val outputFileUri = getCaptureImageOutputUri()
        //  List<Intent> allIntents = new ArrayList<>();
        val packageManager = (activity as AppCompatActivity).getPackageManager()

        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }
            //    allIntents.add(intent);
        }
        return captureIntent
    }

    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage = (activity as AppCompatActivity).getExternalFilesDir("")
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage!!.getPath(), "profile.png"))
        }
        return outputFileUri
    }


    fun takeFile()
    {
        FilePickerBuilder.instance.setMaxCount(1)
            .setSelectedFiles(filepath)
            .setActivityTheme(R.style.LibApp)
            .pickFile(this,FILE);
    }


    private fun checkPermission(): Boolean {
        //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        val result = ContextCompat.checkSelfPermission((activity as AppCompatActivity).getApplicationContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission((activity as AppCompatActivity).getApplicationContext(),
            Manifest.permission.CAMERA
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions((activity as AppCompatActivity), arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ), PERMISSION_REQUEST_CODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = ProgressDialog(view.context)
    }

    fun toast(text:String)
    {
        val inflater = LayoutInflater.from(context as AppCompatActivity)
        val layout = inflater.inflate(R.layout.custom_toast_layout, (context as AppCompatActivity).findViewById(R.id.custom_toast_layout))
       // layout.custom_toast_image.setImageDrawable(ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.ic_home))
        val drawable = ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.toast_round_background)
        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context as AppCompatActivity, R.color.toast), PorterDuff.Mode.MULTIPLY)
        layout.background = drawable
        layout.custom_toast_message.setTextColor(Color.WHITE)
        layout.custom_toast_message.text = text
        /* font?.let {
             layout.custom_toast_message.typeface = font
         }*/
        val toast = Toast(context as AppCompatActivity)
        toast.duration = Toast.LENGTH_SHORT
        if (80 == 80) {
            toast.setGravity(80, 0, 80)
        } else {
            toast.setGravity(80, 0, 0)
        }
        toast.view = layout//setting the view of custom toast layout
        toast.show()

    }

}