package lu.hao.cryptowatchers.view.fragment

import android.graphics.Rect
import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_coin_list.*
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import lu.hao.cryptowatchers.view.adapter.CoinMarketCapAdapter
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.data.Data
import lu.hao.cryptowatchers.viewmodel.TopCoinsViewModel

class TopCoinsFragment : Fragment() {

    private val TAG = "TopCoinsFragment"
    private var mAdapter: CoinMarketCapAdapter = CoinMarketCapAdapter(mutableListOf())
    private val mViewModel = TopCoinsViewModel()
    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_coin_list, container,false)
    }

    // Accessing too soon onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener {
            getCoins()
        }

        recycler_view.layoutManager =
            LinearLayoutManager(activity)
        recycler_view.adapter = mAdapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        Log.d(TAG, "$spacingInPixels")
        recycler_view.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // API call
        swipe_refresh.isRefreshing = true
        getCoins()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

    private fun getCoins() {
        mCompositeDisposable.
                add(mViewModel.getTopCoins()
                        .subscribe(
                {
                    mAdapter.mCoins = translateToOldCoin(it.data)
                    mAdapter.notifyDataSetChanged()
                },
                {
                    Log.d(TAG, it.message!!)
                },
                {
                    Log.d(TAG, "onComplete")
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
        ))
    }

    private fun translateToOldCoin(list: List<Data>): MutableList<Coin> {
        val newList = mutableListOf<Coin>()
        list.forEach {  data ->
            newList.add(Coin(
                data.id,
                data.name,
                data.symbol,
                data.quote.USD.price,
                data.quote.USD.volume_24h,
                data.quote.USD.market_cap,
                data.total_supply,
                data.max_supply,
                data.quote.USD.percent_change_1h,
                data.quote.USD.percent_change_24h,
                data.quote.USD.percent_change_7d
            ))
        }
        return newList
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
