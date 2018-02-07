package lu.hao.cryptowatchers.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_coin_list.*
import io.reactivex.disposables.CompositeDisposable
import lu.hao.cryptowatchers.view.adapter.CoinMarketCapAdapter
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.view.misc.SpacingItemDecoration
import lu.hao.cryptowatchers.viewmodel.StarredCoinsViewModel

class StarredCoinsFragment : Fragment() {

    private val TAG = "StarredCoinsFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(mutableListOf())
    private val mViewModel = StarredCoinsViewModel()
    private val mCompositeDisposable = CompositeDisposable()

    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mPrefListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mSharedPreferences = activity.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        return inflater?.inflate(R.layout.fragment_coin_list, container,false)
    }

    // Accessing too soon onCreateView
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(SpacingItemDecoration())


        // Get the data for all the favorites
        val listOfObservables: MutableList<Observable<MutableList<Coin>>> = mutableListOf()
        val favorites = mSharedPreferences.all.keys
        for (id in favorites) {
            val observableFav = mViewModel.getStarredCoinData(id)
            listOfObservables.add(observableFav)
            getStarredCoin(observableFav)
        }

        setPreferencesListener()

        // Initial API call
        subscribeObservableList(listOfObservables)

        swipe_refresh.setOnRefreshListener {
            mAdapter.mCoins = mutableListOf()
            mCompositeDisposable.clear()
            for (obs in listOfObservables) {
                getStarredCoin(obs)
            }
        }
    }

    private fun setPreferencesListener() {
        mPrefListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (prefs.contains(key)) {
                getStarredCoin(mViewModel.getStarredCoinData(key))
                mAdapter.notifyDataSetChanged()
            }
            else {
                // ConcurrentModificationError
                var removeCoin: Coin? = null
                for (coin in mAdapter.mCoins) {
                    if (coin.id == key) removeCoin = coin
                }
                if (removeCoin != null) mAdapter.mCoins.remove(removeCoin)
                mAdapter.notifyDataSetChanged()
            }
        }
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPrefListener)
    }

    // Wait for all the coins' data (Observables) and then update the RecyclerView
    private fun subscribeObservableList(listOfObservables: MutableList<Observable<MutableList<Coin>>>) {
        Observable.fromIterable(listOfObservables)
                .concatMap({ t: Observable<MutableList<Coin>> -> t.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())})
                .subscribe(
                        {
                            mAdapter.notifyDataSetChanged()
                        },
                        { Log.d(TAG, it.message) },
                        {
                            Log.d(TAG, "onComplete")
                            if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                        })
    }

    //  Disposable
    private fun getStarredCoin(observable: Observable<MutableList<Coin>>) {
        mCompositeDisposable.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    mAdapter.mCoins.add(it[0])
                                },
                                { Log.d(TAG, it.message) },
                                { Log.d(TAG, "onComplete")
                                    if (swipe_refresh.isRefreshing)
                                        swipe_refresh.isRefreshing = false}))
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}
