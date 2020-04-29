package com.example.myapplication.homeview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FooterViewBinding
import com.example.myapplication.databinding.HomeproductlistviewBinding
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.util.ApiStatus

class OfferProductAdapter (private val onClickListener: OnClick ) :  ListAdapter<OfferProducts, RecyclerView.ViewHolder>(completeListDiffCallback()) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = ApiStatus.LOADING

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as OfferProductAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        else (holder as OfferProductAdapter.LoadingHolder).bind(state)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) OfferProductAdapter.ViewHolder.from(parent) else OfferProductAdapter.LoadingHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: HomeproductlistviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: OfferProducts, clickListener: OnClick, position:Int) {
            binding.productData = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeproductlistviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class LoadingHolder private constructor(val binding: FooterViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind( position: ApiStatus) {
//            binding.productData = item
//            binding.index = position
//            binding.clickListener = clickListener
//            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): LoadingHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FooterViewBinding.inflate(layoutInflater, parent, false)
                return LoadingHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount())
        {
            Log.e("CHANGEVIEW","DATA_VIEW_TYPE is called.....")
            DATA_VIEW_TYPE
        } else
        {
            Log.e("CHANGEVIEW","FOOTER_VIEW_TYPE is called.....")
            FOOTER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == ApiStatus.LOADING || state == ApiStatus.ERROR)
    }

    fun setState(state: ApiStatus) {
        this.state = state
        Log.e("CHANGEVIEW","ApiStatus state is...."+state)
        notifyItemChanged(super.getItemCount())
    }

    /*fun setState(state: ApiStatus) {
        this.state = state
        Log.e("CHANGEVIEW","ApiStatus state is...."+state)
        notifyItemChanged(super.getItemCount())
    }*/
}

class OnClick(val clickListener: (marsProperty: OfferProducts, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: OfferProducts, index:Int) = clickListener(marsProperty,1,index)
    fun onAdd(marsProperty: OfferProducts, index:Int) = clickListener(marsProperty,2,index)
    fun onSub(marsProperty: OfferProducts, index:Int) = clickListener(marsProperty,3,index)
    fun onWishList(marsProperty: OfferProducts, index:Int) = clickListener(marsProperty,4,index)
    fun onDetail(marsProperty: OfferProducts, index:Int) = clickListener(marsProperty,5,index)
    fun prizeDetail(marsProperty: OfferProducts,index: Int) =clickListener(marsProperty,6,index)
}

class completeListDiffCallback : DiffUtil.ItemCallback<OfferProducts>() {

    override fun areItemsTheSame(oldItem: OfferProducts, newItem: OfferProducts): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: OfferProducts, newItem: OfferProducts): Boolean {
        return oldItem == newItem
    }
}