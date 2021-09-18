package com.rmaproject.myqoran.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rmaproject.myqoran.ui.aboutus.AboutUsFragment
import com.rmaproject.myqoran.ui.rating.RatingFragment


class ViewPageAdapter(context: Fragment, val fragmentList: List<Fragment>) : FragmentStateAdapter(context) {

    //Banyaknya Fragment yang akan ditampilkan
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    //Nama Class yang ada di Fragment Kotlin
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}