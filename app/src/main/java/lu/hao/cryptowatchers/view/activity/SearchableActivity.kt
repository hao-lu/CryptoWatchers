package lu.hao.cryptowatchers.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_searchable.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.DividerItemDecoration
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.api.CoinMarketCapApi
import lu.hao.cryptowatchers.model.data.MapResponse
import lu.hao.cryptowatchers.view.adapter.SearchResultsAdapter

class SearchableActivity : AppCompatActivity() {

    private val TAG = "SearchableActivity"
    private var mCryptocurrencies: MutableList<Coin>? = null

    private var mAdapter: SearchResultsAdapter = SearchResultsAdapter(mutableListOf())

    private val mCoinMarketObservable: Observable<MapResponse> = CoinMarketCapApi.create()
            .getCrytocurrenyMap()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

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
                {
                    mCryptocurrencies = translateToNewApiResponse(it)
                }, //onNext
                {
                    Log.d(TAG, it.message)
                }, //onError
                { Log.d(TAG, "onComplete") } // onComplete
        )

        mTextChangeObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val results = search(it)
                            Log.d(TAG, "SIZE: " + results.size)
                            mAdapter.mCoins = results
                            mAdapter.notifyDataSetChanged()
                        },
                        { Log.d(TAG, it.message) },
                        { Log.d(TAG, "onComplete") }
                )

        // Set onClickListener for back button
        back_button.setOnClickListener({ onBackPressed() })
        // Set onClickListener for clear button
        clear_button.setOnClickListener({ search_edit_text.text.clear() })

        search_results_list.layoutManager =
            LinearLayoutManager(this)
        search_results_list.adapter = mAdapter
        val dividerItemDecoration =
            DividerItemDecoration(
                search_results_list.context,
                LinearLayoutManager(this).orientation
            )
        search_results_list.addItemDecoration(dividerItemDecoration)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun search(s: String): MutableList<Coin> {
        if (s == "") return mutableListOf()
         return mCryptocurrencies!!.filter { it.name.toLowerCase().startsWith(s.toLowerCase())
                 || it.symbol.toLowerCase().startsWith(s.toLowerCase()) } as MutableList<Coin>
    }

    private fun translateToNewApiResponse(mapResponse: MapResponse): MutableList<Coin> {
        val list = mutableListOf<Coin>()
        mapResponse.data.forEach { data ->
            list.add(Coin(
                data.id,
                data.name,
                data.symbol,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            ))
        }
        return list
    }
}