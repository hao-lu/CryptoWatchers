package lu.hao.cryptowatchers

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_coin_details.*

class CoinDetailsActivity : AppCompatActivity() {

    private val TAG = "CoinDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)
        setSupportActionBar(toolbar)

        // Enable and show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val coin = intent.extras.getParcelable<Coin>("Coin")
        Log.d(TAG, "" + coin.priceUsd)
        supportActionBar?.title = "${coin.symbol} | ${coin.name} "

        tab_layout.addTab(tab_layout.newTab().setText("24H"))
        tab_layout.addTab(tab_layout.newTab().setText("7D"))
        tab_layout.addTab(tab_layout.newTab().setText("1M"))
        tab_layout.addTab(tab_layout.newTab().setText("3M"))
        tab_layout.addTab(tab_layout.newTab().setText("6M"))
        tab_layout.addTab(tab_layout.newTab().setText("1Y"))

        // Initialized data in chart fragment
        val chartFragment = ChartFragment()
        val args = Bundle()
        args.putString("period", "1day")
        args.putParcelable("coin", coin)
        chartFragment.arguments = args
        supportFragmentManager
                .beginTransaction()
                .add(R.id.chart_container, chartFragment, "chartFragment")
                .commit()

        // Coin info fragment
        val coinInfoFragment = CoinInfoFragment()
        coinInfoFragment.arguments = args
        supportFragmentManager
                .beginTransaction()
                .add(R.id.info_container, coinInfoFragment, "coinInfoFragment")
                .commit()

        tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = ChartFragment()

                when (tab.position) {
                    0 -> args.putString("period", "1day")
                    1 -> args.putString("period", "7day")
                    2 -> args.putString("period", "30day")
                    3 -> args.putString("period", "90day")
                    4 -> args.putString("period", "180day")
                    else -> {
                        args.putString("period", "365day")
                    }
                }
                fragment.arguments = args

                chart_progress_bar.visibility = ProgressBar.VISIBLE
                chart_container.visibility = ProgressBar.GONE

                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.chart_container, fragment, "coinListFragment")
                        .commit()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}