package lu.hao.cryptowatchers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.activity_base_content, TopCoinsFragment.newInstance(), "coinListFragment")
//                    .commit()
//        }

        container.adapter = SectionsPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(container)

        tab_layout.getTabAt(0)!!.setIcon(R.drawable.ic_top_coins_24dp)
        tab_layout.getTabAt(1)!!.setIcon(R.drawable.ic_favorite_24dp)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val intent = Intent(this, SearchableActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
