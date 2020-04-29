package com.example.myapplication.faqView

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.network.Offers
import com.zakariachowdhury.cityschools.CustomExpandableListAdapter
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import kotlinx.android.synthetic.main.faqview.*

class FAQView : Fragment() {

    lateinit var faqsListView: ExpandableListView
    lateinit var faqsView: View


    lateinit var infoTextView: TextView
    lateinit var expandableListView: ExpandableListView
    lateinit var expandableListAdapter: CustomExpandableListAdapter

    private lateinit var cityNames: ArrayList<String>
    private lateinit var citySchoolsMap: HashMap<String, ArrayList<String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        faqsView = inflater?.inflate(R.layout.faqview, container, false)
      var  backarrow = faqsView?.findViewById(R.id.backarrow) as ImageView
       // infoTextView = faqsView!!.findViewById(R.id.infoTextView)

        expandableListView = faqsView!!.findViewById(R.id.expandableListView)
        cityNames = ArrayList()
        citySchoolsMap = HashMap()

        expandableListAdapter = CustomExpandableListAdapter(activity as AppCompatActivity, cityNames, citySchoolsMap)
        expandableListView.setAdapter(expandableListAdapter)
        var imageModel: MutableList<Faqquestion>? =  mutableListOf()
        var offers: MutableList<Offers>? =  mutableListOf()

        offers!!.add(Offers(1,"ddd"))
        var citySchoolsMapsss: HashMap<String, ArrayList<String>>
        citySchoolsMapsss = HashMap()

        var answer: ArrayList<String>? =  ArrayList()
        answer!!.add("Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.\n")
        citySchoolsMapsss.put("How to pay amazon.",answer)
        citySchoolsMapsss.put("There are many more.", answer)
        citySchoolsMapsss.put("How to enable two factor authentication.", answer)
        citySchoolsMapsss.put("How to contact us.", answer)
        populateExpandableListView(citySchoolsMapsss)


        backarrow.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        return faqsView
   }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun hideLoader(dataFound: Boolean) {
        if (dataFound) {
            infoTextView.visibility = View.GONE
        } else {
            infoTextView.text = "No data found"
        }
    }



    private fun populateExpandableListView(citySchoolsHashMap: HashMap<String, ArrayList<String>>) {



        cityNames.addAll(citySchoolsHashMap.keys.sorted())
        citySchoolsMap.putAll(citySchoolsHashMap)
        expandableListAdapter.notifyDataSetChanged()
    }



}