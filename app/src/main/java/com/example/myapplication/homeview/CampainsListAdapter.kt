package com.example.myapplication.homeview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ProductHomeListBinding
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Offers

class CampainsListAdapter(private val onClickListener: OnClickListener):  ListAdapter<Offers, CampainsListAdapter.ViewHolder>(ProductListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
       holder.itemView.setOnClickListener {
            onClickListener.onClick(item,position)
        }
        holder.bind(item,position)
    }
   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(ProductHomeListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class ViewHolder(private var binding: ProductHomeListBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: Offers,position:Int) {

            binding.offer = marsProperty
            binding.index = position
          //  binding.clickListener = clickListener
            // This is important, because it forces the data binding to execute immediately,
            binding.executePendingBindings()

        }
    }

    class OnClickListener(val clickListener: (marsProperty:Offers,type:Int, index:Int) -> Unit) {
       // fun onClick(marsProperty:Offers) = clickListener(marsProperty)
        fun onClick(marsProperty: Offers, index:Int) = clickListener(marsProperty,1,index)
    }
}

class ProductListDiffCallback : DiffUtil.ItemCallback<Offers>() {

    override fun areItemsTheSame(oldItem: Offers, newItem: Offers): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Offers, newItem: Offers): Boolean {
        return oldItem == newItem
    }
}
