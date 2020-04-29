package com.example.myapplication.product

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FooterViewBinding
import com.example.myapplication.databinding.ProductlistBinding
import com.example.myapplication.network.Product
import com.example.myapplication.util.ApiStatus

class ProductListAdapter (private val onClickListener: OnClick ) :  ListAdapter<Product, RecyclerView.ViewHolder>(completeListDiffCallback()) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = ApiStatus.LOADING

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as ProductListAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        else (holder as ProductListAdapter.LoadingHolder).bind(state)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) ProductListAdapter.ViewHolder.from(parent) else ProductListAdapter.LoadingHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ProductlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Product, clickListener: OnClick, position:Int) {
            binding.viewmodel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductlistBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


        //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjMsImlzcyI6Imh0dHBzOi8va2Fuei5hcHAvZGVtby9hcGkvdXNlci9sb2dpbiIsImlhdCI6MTU4MTQwODAzOCwiZXhwIjoxNTgxNDExNjM4LCJuYmYiOjE1ODE0MDgwMzgsImp0aSI6ImQ2alJZMk52bGI1VG85OVUifQ.12LqXBXPDfWwCsiuuT3gk4p43SyCWww8tNk9EqUIoDE

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

class OnClick(val clickListener: (marsProperty: Product, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: Product, index:Int) = clickListener(marsProperty,1,index)
    fun onDelete(marsProperty: Product, index:Int) = clickListener(marsProperty,2,index)
}

class completeListDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}