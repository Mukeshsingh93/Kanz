package com.example.myapplication.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FooterViewBinding
import com.example.myapplication.databinding.OrderlistviewBinding
import com.example.myapplication.network.GetCartList
import com.example.myapplication.util.ApiStatus

class OrderListAdapter (private val onClickListener: OnClick ) :  ListAdapter<GetCartList, RecyclerView.ViewHolder>(completeListDiffCallback()) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = ApiStatus.LOADING

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as OrderListAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        else (holder as OrderListAdapter.LoadingHolder).bind(state)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) OrderListAdapter.ViewHolder.from(parent) else OrderListAdapter.LoadingHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: OrderlistviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: GetCartList, clickListener: OnClick, position:Int) {
            binding.viewmodel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrderlistviewBinding.inflate(layoutInflater, parent, false)
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

class OnClick(val clickListener: (marsProperty: GetCartList, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: GetCartList, index:Int) = clickListener(marsProperty,1,index)
    fun onDelete(marsProperty: GetCartList, index:Int) = clickListener(marsProperty,2,index)
}

class completeListDiffCallback : DiffUtil.ItemCallback<GetCartList>() {

    override fun areItemsTheSame(oldItem: GetCartList, newItem: GetCartList): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: GetCartList, newItem: GetCartList): Boolean {
        return oldItem == newItem
    }
}