package lu.hao.cryptowatchers

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_coin_details.*

class CoinDetailsActivity : AppCompatActivity() {

    private val TAG = "CoinDetailsActivity"

    private lateinit var mChartPagerAdapter: ChartPagerAdapter
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)
        setSupportActionBar(toolbar)

        // Enable and show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val coin = intent.extras.getParcelable<Cryptocurrency>("Cryptocurrency")
        Log.d(TAG, coin.name)
        supportActionBar?.title = "${coin.symbol} | ${coin.name} "

        mViewPager = container
        mChartPagerAdapter = ChartPagerAdapter(supportFragmentManager, coin.symbol)
        mViewPager.adapter = mChartPagerAdapter

        tab_layout.setupWithViewPager(mViewPager)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}