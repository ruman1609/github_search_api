package com.rudyrachman16.githubuserapi.view.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rudyrachman16.githubuserapi.view.TabFragment
import com.rudyrachman16.githubuserapi.view.activities.DetailActivity

class TabAdapter(activity: AppCompatActivity, val userPlace: Array<String>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = DetailActivity.tabTitle.size

    override fun createFragment(position: Int): Fragment =
        TabFragment.newInstance(userPlace[position], position)
}