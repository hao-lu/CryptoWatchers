package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

//        val subscription = CryptoManager().getCryptos()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { retrievedCryptos ->
//                            recyclerView.adapter = CoinMarketCapAdapter(retrievedCryptos)
//
//                        },
//                        { error ->
//                            Log.d(TAG, error.message)},
//                        {Log.d(TAG, "onCompleted")}
//                )

        // Without using the created Observable from Retrofit
//        val observable = Observable.create(ObservableOnSubscribe<List<Cryptocurrency>> {
//            emitter ->
//            val response = CoinMarketCapApi.create().getTickerLimit("10")
//            response.enqueue(object: Callback<List<Cryptocurrency>> {
//                override fun onResponse(call: Call<List<Cryptocurrency>>, response: Response<List<Cryptocurrency>>) {
//                    if (response.isSuccessful) {
//                        emitter.onNext(response.body()!!)
//                        emitter.onComplete()
//                    }
//
//                    else {
//                        Log.d(TAG, "onResponse is not successful")
//                        emitter.onError(Throwable(response.message()))
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Cryptocurrency>>, t: Throwable) {
//                    Log.d(TAG, "onFailure")
//                }
//            })
//        })


//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { retrievedCryptos ->
//                            recyclerView.adapter = CoinMarketCapAdapter(retrievedCryptos)
//
//                        },
//                        { error ->
//                            Log.d(TAG, error.message)},
//                        {Log.d(TAG, "onCompleted")}
//
//
//                )

        return view
    }

}
