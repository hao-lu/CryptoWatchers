package lu.hao.cryptowatchers

import android.graphics.Rect
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
import kotlinx.android.synthetic.main.fragment_coin_list.*
import android.support.v7.widget.RecyclerView

class TopCoinsFragment : Fragment() {

    private val TAG = "TopCoinsFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(mutableListOf())

    private val mObservable: Observable<MutableList<Coin>> = CoinMarketCapApi.create()
            .getTickerLimitObservable("25")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val mObserver = object : Observer<MutableList<Coin>> {
        override fun onComplete() {
            Log.d(TAG, "onComplete")
            if (swipe_refresh.isRefreshing)
                swipe_refresh.isRefreshing = false
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, e.message)
        }

        override fun onNext(coins: MutableList<Coin>) {
            // Update the data in the adapter
            mAdapter.mCoins = coins
            mAdapter.notifyDataSetChanged()
        }

        override fun onSubscribe(d: Disposable) {}
    }

    companion object {
        fun newInstance(): TopCoinsFragment {
            return TopCoinsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_coin_list, container,false)
    }

    // Accessing too soon onCreateView
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener {
            mObservable.subscribe(mObserver)
        }

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = mAdapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        Log.d(TAG, "$spacingInPixels")
        recycler_view.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // API call
        swipe_refresh.isRefreshing = true
        mObservable.subscribe(mObserver)
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
