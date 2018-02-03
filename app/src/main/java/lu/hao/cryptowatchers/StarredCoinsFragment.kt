package lu.hao.cryptowatchers

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
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
import android.support.v7.widget.RecyclerView

class StarredCoinsFragment : Fragment() {

    private val TAG = "StarredCoinsFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(mutableListOf())
    private val mBaseApi = CoinMarketCapApi.create()
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    companion object {
        fun newInstance(): TopCoinsFragment {
            return TopCoinsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mSharedPreferences = activity.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        return inflater?.inflate(R.layout.fragment_coin_list, container,false)
    }

    // Accessing too soon onCreateView
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = mAdapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recycler_view.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // Get the data for all the favorites
        val listOfObservables: MutableList<Observable<MutableList<Coin>>> = mutableListOf()
        val favorites = mSharedPreferences.all.keys
        for (fav in favorites) {
            val observableFav = getCoinInfoObservable(fav)
            listOfObservables.add(observableFav)
            getCoinInfoObserver(observableFav)
//            observableFav
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                    {
//                        mAdapter.mCoins.add(it[0])
//                        mAdapter.notifyDataSetChanged()
//                    },
//                    { Log.d(TAG, it.message) },
//                    { Log.d(TAG, "onComplete")}
//            )
        }

//        val ob = Observable.create(ObservableOnSubscribe<SharedPreferences.OnSharedPreferenceChangeListener> {
//            emitter ->
//        })


        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (prefs.contains(key)) {
                getCoinInfoObserver(getCoinInfoObservable(key))
            }
            else {
                for (coin in mAdapter.mCoins) {
                    if (coin.id == key) mAdapter.mCoins.remove(coin)
                }
                mAdapter.notifyDataSetChanged()
            }
        }


        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // Initial API call
        val observableWhenAll = Observable.fromIterable(listOfObservables)
//        subscribeObservableList(observableWhenAll)

        swipe_refresh.setOnRefreshListener {
//            subscribeObservableList(observableWhenAll)
            mAdapter.mCoins = mutableListOf()
            for (obs in listOfObservables) {
                getCoinInfoObserver(obs)
            }
        }
    }

//    private fun subscribeObservableList(observable: Observable<Observable<MutableList<Coin>>>) {
//        observable.concatMap({ t: Observable<MutableList<Coin>> -> t.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())})
//                .subscribe(
//                        {
////                            mAdapter.mCoins = coins
//                            mAdapter.notifyDataSetChanged() },
//                        { Log.d(TAG, it.message) },
//                        {
//                            Log.d(TAG, "onComplete")
//                            if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
//                        })
//    }


    // Disposable
    private fun getCoinInfoObserver(observable: Observable<MutableList<Coin>>) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            mAdapter.mCoins.add(it[0])
                            mAdapter.notifyDataSetChanged()
                        },
                        { Log.d(TAG, it.message) },
                        { Log.d(TAG, "onComplete")
                            if (swipe_refresh.isRefreshing)
                                swipe_refresh.isRefreshing = false})
    }

    private fun getCoinInfoObservable(id: String): Observable<MutableList<Coin>> {
        return mBaseApi.getSpecificTickerLimitObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space - 4
            } else {
                // Remove the extra spacing from attribute: cardUseCompatPadding
                outRect.top = -space
            }
        }
    }
}
