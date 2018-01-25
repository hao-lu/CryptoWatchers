package lu.hao.cryptowatchers

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinCapApi {

    // Need two path annotations for 1day/BTC
    @GET("history/{path}/{path2}")
    fun getHistoryObservable(@Path("path") path: String, @Path("path2") path2: String): Observable<CoinHistory>

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