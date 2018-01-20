package lu.hao.cryptowatchers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.Button
import kotlinx.android.synthetic.main.activity_searchable.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import android.support.v7.widget.DividerItemDecoration

class SearchableActivity : AppCompatActivity() {

    private val TAG = "SearchableActivity"
    private var mCryptocurrencies: List<Cryptocurrency>? = null

    private var mAdapter: SearchResultsAdapter = SearchResultsAdapter(emptyList())


    private val mCoinMarketObservable: Observable<List<Cryptocurrency>> = CoinMarketCapApi.create()
            .getTickerLimitObservable("0")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val mTextChangeObserver = object : Observer<String> {
        override fun onComplete() {}

        override fun onError(e: Throwable) { Log.d(TAG, e.message) }

        override fun onNext(s: String) {
            // Update the data in the adapter
            Log.d(TAG, s)
        }

        override fun onSubscribe(d: Disposable) {}
    }

    private val mTextChangeObservable: Observable<String> = Observable.create {
        emitter ->
        search_edit_text.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (p0.toString() == "") clear_button.visibility = Button.INVISIBLE
                else clear_button.visibility = Button.VISIBLE
            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emitter.onNext(p0.toString())
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        setSupportActionBar(toolbar)
        // Remove title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Enable and show back button
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        mCoinMarketObservable.subscribe(
                { mCryptocurrencies = it }, //onNext
                { Log.d(TAG, it.message)}, //onError
                { Log.d(TAG, "onComplete") } // onComplete
        )

        mTextChangeObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val results = search(it)
                            Log.d(TAG, "SIZE: " + results.size)
                            mAdapter.mCryptos = results
                            mAdapter.notifyDataSetChanged()
                        },
                        { Log.d(TAG, it.message) },
                        { Log.d(TAG, "onComplete") }
                )

        back_button.setOnClickListener({ onBackPressed() })

        clear_button.setOnClickListener({ search_edit_text.text.clear() })



        search_results_list.layoutManager = LinearLayoutManager(this)
        search_results_list.adapter = mAdapter
        val dividerItemDecoration = DividerItemDecoration(search_results_list.context,
                LinearLayoutManager(this).orientation)
        search_results_list.addItemDecoration(dividerItemDecoration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search, menu)
//        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        // Remove the extra padding between the SearchView and back arrow
//        val searchEditFrame = searchView.findViewById<LinearLayout>(R.id.search_edit_frame) as LinearLayout
//        (searchEditFrame.layoutParams as LinearLayout.LayoutParams).leftMargin = 0
//
//        searchView.onActionViewExpanded()
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun search(s: String): List<Cryptocurrency> {
        if (s == "") return emptyList()
         return mCryptocurrencies!!.filter { it.name.toLowerCase().startsWith(s.toLowerCase())
                 || it.symbol.toLowerCase().startsWith(s.toLowerCase()) }
    }
}