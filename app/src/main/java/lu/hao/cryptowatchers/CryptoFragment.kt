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
import kotlinx.android.synthetic.main.cryptos_fragment.*

class CryptoFragment : Fragment() {

    private val TAG = "CryptoFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(emptyList())

    private val mObservable: Observable<List<Cryptocurrency>> = CoinMarketCapApi.create()
            .getTickerLimitObservable("25")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val mObserver = object : Observer<List<Cryptocurrency>> {
        override fun onComplete() {
            Log.d(TAG, "onComplete")
            if (swipe_refresh.isRefreshing)
                swipe_refresh.isRefreshing = false
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, e.message)
        }

        override fun onNext(coins: List<Cryptocurrency>) {
            // Update the data in the adapter
            mAdapter.mCryptos = coins
            mAdapter.notifyDataSetChanged()
        }

        override fun onSubscribe(d: Disposable) {}
    }

    companion object {
        fun newInstance(): CryptoFragment {
            return CryptoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.cryptos_fragment, container,false)
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
