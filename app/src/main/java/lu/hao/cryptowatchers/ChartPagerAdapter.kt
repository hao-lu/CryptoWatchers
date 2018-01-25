package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.os.Bundle

class ChartPagerAdapter(fm: FragmentManager, symbol: String) : FragmentStatePagerAdapter(fm) {

    private val mSymbol = symbol
    private val mNumTabs = 3

    override fun getItem(position: Int): Fragment {
        val fragment = ChartFragment()
        val args = Bundle()

        when (position) {
            0 -> args.putString("history", "1day/$mSymbol")
            1 -> args.putString("history", "7day/$mSymbol")
            else -> {
                args.putString("history", "30day/$mSymbol")
            }
        }

        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return mNumTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "1H"
            1 -> "24H"
            else -> "7D"
        }
    }
}

