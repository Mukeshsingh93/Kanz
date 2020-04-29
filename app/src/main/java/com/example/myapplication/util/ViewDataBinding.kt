package com.example.myapplication.util

import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.homeview.CampainsListAdapter
import com.example.myapplication.network.Offers
import com.example.myapplication.network.ProductModel
import com.example.myapplication.network.Transaction
import kotlinx.android.synthetic.main.nav_header.view.*
import java.text.DecimalFormat

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Offers>?) {
    val adapter = recyclerView.adapter as CampainsListAdapter
    adapter.submitList(data)
}


@BindingAdapter("offerProductFromUrl")
fun bindofferProductFromUrl(view: ImageView, imageUrl: String?) {

    Log.e("DYBAMICIMAGES","ondata is called...."+imageUrl.toString())

    if (!imageUrl.isNullOrEmpty()) {
        Log.e("DYBAMICIMAGES","ondata is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("bindProductImageUrl")
fun bindProductImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("LATESTIMAGE","ondata is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load("https://kanz.app/demo/"+imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("CHECKDATA","ondata is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}


@BindingAdapter("profileimage")
fun bindprofileimage(view: ImageView, imageUrl: String?) {


    Log.e("PROFILETAB","profiletab is called...."+imageUrl)

    if (!imageUrl.isNullOrEmpty()) {
        Log.e("PROFILETAB","profiletab is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load(imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
    else{

        Glide.with(view.context)
            .load(R.drawable.userdata)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}



@BindingAdapter("bindImageUrl")
fun bindImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("LATESTIMAGE","ondata is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load("https://kanz.app/demo/"+imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("quanity","price")
fun totalproductprice(textView: TextView, quantity: String?,price: String?) {

   // Log.e("ADDTOPANDU","quantity is..."+quantity)
    //Log.e("ADDTOPANDU","price is...."+price)


    var quantity=  quantity!!.toInt()
    var price=  price!!.toDouble()
    var savePrice:Float = (quantity*price).toFloat()

   // Log.e("ADDTOPANDU","price is...."+savePrice)
    val df = DecimalFormat("###.##")
  //  textView.text ="₹ "+df.format(quantity.toDouble())
    textView.text =df.format(savePrice.toDouble()).toString()
}

@BindingAdapter("transaction")
fun transaction(textView: TextView, transaction: Transaction?) {
    var transactiontext : String =""
    if(transaction!=null)
    {
        transactiontext = transaction.sold_quantity.toString()
    }
    else
    {
        transactiontext = "0"

    }
    Log.e("TRANSACTION","transaction is called......"+transactiontext.toString())
    textView.text =transactiontext
}

@BindingAdapter("transaction","totalquan")
fun transaction(textView: TextView, transaction: Transaction?,totalQuan:String) {
    var transactiontext : String =""
    if(transaction!=null)
    {
        transactiontext = transaction.sold_quantity.toString()
    }
    else
    {

    }
    Log.e("TRANSACTION","transaction is called......"+transactiontext.toString())
    textView.text =totalQuan+"/"+transactiontext
}


@BindingAdapter("totalquantity","transaction")
fun progressbar(progressBar: ProgressBar,totalQuantity:String,transaction: Transaction?) {
    var totalValue :Int= totalQuantity.toInt()
    var soldquantity : Int=0
    if(transaction!=null)
    {
        soldquantity = transaction.sold_quantity!!.toInt()
    }
    else
    {
        soldquantity = 0
    }
    Log.e("PERCENTAGE","percentage totalquantity value....."+totalValue)
    Log.e("PERCENTAGE","percentage soldquantity value....."+soldquantity)


    var percentageValue = (soldquantity / totalValue.toDouble()) * 100

    Log.e("PERCENTAGE","percentage value....."+percentageValue)

    progressBar.setProgress(percentageValue.toInt())
}

@BindingAdapter("bindImageFromDataUrl")
fun bindImageFromDataUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Log.e("LATESTIMAGE","ondata is called...."+imageUrl.toString())
        Glide.with(view.context)
            .load("https://kanz.app/demo/"+imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("likeimage")
fun likeimage(view: ImageView, imageUrl: Boolean?) {
    if (imageUrl==true) {
       Log.e("LATESTIMAGE","ondata is called...."+imageUrl)
        Glide.with(view.context)
            .load(R.drawable.star_fill)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
    else
    {
        Glide.with(view.context)
            .load(R.drawable.star)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}
