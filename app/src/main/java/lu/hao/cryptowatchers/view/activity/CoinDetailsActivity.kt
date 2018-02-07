package lu.hao.cryptowatchers.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_coin_details.*
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.view.fragment.ChartFragment
import lu.hao.cryptowatchers.view.fragment.CoinInfoFragment

class CoinDetailsActivity : AppCompatActivity() {

    private val TAG = "CoinDetailsActivity"
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var coin: Coin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)
        setSupportActionBar(toolbar)

        mSharedPreferences = this.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

        // Enable and show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        coin = intent.extras.getParcelable<Coin>("Coin")
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)

        // Favorite or unfavorite icon based on user preference
        val isFavorite = mSharedPreferences.getBoolean(coin.id, false)
        if (isFavorite)
            menu.getItem(0).setIcon(R.drawable.ic_favorite_24dp)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            // Unfavorite
            if (mSharedPreferences.contains(coin.id)) {
                item.setIcon(R.drawable.ic_unfavorite_24dp)
                with(mSharedPreferences.edit()) {
                    remove(coin.id)
                    apply()
                }
            }
            // Favorite
            else {
                item.setIcon(R.drawable.ic_favorite_24dp)
                with(mSharedPreferences.edit()) {
                    putBoolean(coin.id, true)
                    apply()
                }
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}