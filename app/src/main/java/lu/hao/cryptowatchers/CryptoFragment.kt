package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CryptoFragment : Fragment() {

    private val TAG = "CryptoFragment"

    companion object {
        fun newInstance(): CryptoFragment {
            return CryptoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater?.inflate(R.layout.cryptos_fragment, container,false)

        val activity = activity
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val observable = CoinMarketCapApi.create().getTickerLimitObservable("10")
        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrievedCryptos ->
                            recyclerView.adapter = CoinMarketCapAdapter(retrievedCryptos)

                        },
                        { error ->
                            Log.d(TAG, error.message)},
                        {Log.d(TAG, "onCompleted")}
                )


        return view
    }

}
