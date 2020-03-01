package lu.hao.cryptowatchers.viewmodel

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.api.CoinMarketCapApi
import lu.hao.cryptowatchers.model.data.LatestListingResponse

class TopCoinsViewModel : ViewModel {

    private val TAG = "TopCoinsViewModel"

    private val mObservable = CoinMarketCapApi.create()
            .getLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun onCreate() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onDestroy() {}

    fun getTopCoins(): Observable<LatestListingResponse> {
        return mObservable
    }
}
