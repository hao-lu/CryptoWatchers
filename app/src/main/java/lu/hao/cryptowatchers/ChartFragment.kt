package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.Result.response
import com.google.gson.GsonBuilder



class ChartFragment : Fragment() {

    private val TAG = "ChartFragment"

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_chart, container, false)
        val url = arguments.getString("history")
        Log.d(TAG, url)

        val observable = CoinCapApi.create().getHistoryObservable("1day", "BTC")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val disposable = observable.subscribe(
                {history -> Log.d(TAG, "API CALL : " + GsonBuilder().setPrettyPrinting().create().toJson(history)) },
                {e-> Log.d(TAG, e.message)},
                {Log.d(TAG, "onComplete")}
        )
        return rootView
    }
}