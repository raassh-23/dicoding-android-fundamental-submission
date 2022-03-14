package com.raassh.dicodinggithubuserapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raassh.dicodinggithubuserapp.view.fragment.FollowFragment

class FollowSectionsPagerAdapter(activity: AppCompatActivity, val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position)
            putString(FollowFragment.ARG_USERNAME, username)
        }

        return fragment
    }
}