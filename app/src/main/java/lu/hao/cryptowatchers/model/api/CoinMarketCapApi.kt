package lu.hao.cryptowatchers.model.api

import io.reactivex.Observable
import lu.hao.cryptowatchers.model.data.Coin
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinMarketCapApi {

    // Using Call<List<Coin>> rather than Call<CryptoResponse> because API is returning a JSONArray rather than JSONObject
    @GET("v1/ticker/")
    fun getTickerLimitObservable(@Query("limit") limit: String): Observable<MutableList<Coin>>

    @GET("v1/ticker/{coin}")
    fun getSpecificTickerLimitObservable(@Path("coin") coin: String): Observable<MutableList<Coin>>

    companion object Factory {
        fun create(): CoinMarketCapApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.coinmarketcap.com/")
                    // Add because it is type Observable, need an adapter for the data
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(CoinMarketCapApi::class.java)
        }
    }
}
