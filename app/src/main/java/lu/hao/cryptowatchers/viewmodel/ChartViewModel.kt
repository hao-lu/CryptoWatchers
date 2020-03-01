package lu.hao.cryptowatchers.viewmodel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lu.hao.cryptowatchers.model.api.CoinCapApi
import lu.hao.cryptowatchers.model.data.CoinHistory
import lu.hao.cryptowatchers.model.data.HistoryPriceResponse

class ChartViewModel : ViewModel {

    override fun onCreate() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onDestroy() {}

    fun getCoinHistoryData(period: String, symbol: String): Observable<HistoryPriceResponse> {
        return CoinCapApi.create().getHistoryObservable(period, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
