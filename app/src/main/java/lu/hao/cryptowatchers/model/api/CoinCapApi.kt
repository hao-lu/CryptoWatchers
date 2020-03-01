package lu.hao.cryptowatchers.model.api

import io.reactivex.Observable
import lu.hao.cryptowatchers.model.data.CoinHistory
import lu.hao.cryptowatchers.model.data.HistoryPriceResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// Use for charts
interface CoinCapApi {

    // Need two path annotations for 1day/BTC
    //https://api.coincap.io/v2/assets/tether/history?interval=d1
    @GET("{symbol}/history")
    fun getHistoryObservable(@Path("symbol") symbol: String, @Query("interval") interval: String): Observable<HistoryPriceResponse>

    companion object Factory {
        fun create(): CoinCapApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.coincap.io/v2/assets/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CoinCapApi::class.java)
        }
    }
}
