package lu.hao.cryptowatchers.viewmodel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.api.CoinMarketCapApi

class StarredCoinsViewModel : ViewModel {

    private val listOfObservables: MutableList<Observable<MutableList<Coin>>> = mutableListOf()

    override fun onCreate() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onDestroy() {}

    fun getStarredCoinData(id: String): Observable<MutableList<Coin>> {
        return CoinMarketCapApi.create()
                .getSpecificTickerLimitObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}