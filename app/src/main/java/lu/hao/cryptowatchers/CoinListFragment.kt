package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cryptos.*

class CoinListFragment : Fragment() {

    private val TAG = "CoinListFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(emptyList())

    private val mObservable: Observable<List<Coin>> = CoinMarketCapApi.create()
            .getTickerLimitObservable("25")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val mObserver = object : Observer<List<Coin>> {
        override fun onComplete() {
            Log.d(TAG, "onComplete")
            if (swipe_refresh.isRefreshing)
                swipe_refresh.isRefreshing = false
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, e.message)
        }

        override fun onNext(coins: List<Coin>) {
            // Update the data in the adapter
            mAdapter.mCryptos = coins
            mAdapter.notifyDataSetChanged()
        }

        override fun onSubscribe(d: Disposable) {}
    }

    companion object {
        fun newInstance(): CoinListFragment {
            return CoinListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_cryptos, container,false)
    }

    // Accessing too soon onCreateView
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener {
            mObservable.subscribe(mObserver)
        }

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = mAdapter

        // API call
        swipe_refresh.isRefreshing = true
        mObservable.subscribe(mObserver)
    }
}
