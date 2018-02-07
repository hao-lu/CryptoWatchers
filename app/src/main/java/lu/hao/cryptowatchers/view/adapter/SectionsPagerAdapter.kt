package lu.hao.cryptowatchers.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.os.Bundle
import lu.hao.cryptowatchers.view.fragment.StarredCoinsFragment
import lu.hao.cryptowatchers.view.fragment.TopCoinsFragment

class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mNumTabs = 2

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
        val args = Bundle()

        when (position) {
            0 -> fragment = TopCoinsFragment()
            else -> {
                fragment = StarredCoinsFragment()
            }
        }

        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return mNumTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
//        return when (position) {
//            0 -> "TOP"
//            else -> "STARRED"
//
//        }
        return ""
    }
}

