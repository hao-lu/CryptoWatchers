package lu.hao.cryptowatchers.model.api

import io.reactivex.Observable
import lu.hao.cryptowatchers.model.data.CoinHistory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinCapApi {

    // Need two path annotations for 1day/BTC
    @GET("history/{period}/{symbol}")
    fun getHistoryObservable(@Path("period") period: String, @Path("symbol") symbol: String): Observable<CoinHistory>

    companion object Factory {
        fun create(): CoinCapApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://coincap.io/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(CoinCapApi::class.java)
        }
    }
}
