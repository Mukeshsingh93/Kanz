package com.example.myapplication.productdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myapplication.productdetail.AdvertismentView
import com.example.myapplication.network.SliderImages

class ProductBannerAdapter (fragmentManager: FragmentManager, private val movies: MutableList<SliderImages>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        // return AdvertismentView.newInstance(movies[position % movies.size])
        return AdvertismentView.newInstance(movies.get(position))
    }

    // 3
    override fun getCount(): Int {
        //  return movies.size * MAX_VALUE
        return movies.size
    }
//  override fun getPageTitle(position: Int): CharSequence {
//    return movies[position % movies.size].title
//  }
}