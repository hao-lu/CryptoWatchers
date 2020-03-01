package lu.hao.cryptowatchers.model.api

import io.reactivex.Observable
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.data.LatestListingResponse
import lu.hao.cryptowatchers.model.data.MapResponse
import okhttp3.OkHttpClient
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

    @GET("v1/cryptocurrency/listings/latest")
    fun getLatest(): Observable<LatestListingResponse>

    @GET("v1/cryptocurrency/map")
    fun getCrytocurrenyMap(): Observable<MapResponse>

    companion object Factory {
        fun create(): CoinMarketCapApi {
            val okHttp = OkHttpClient.Builder()
            okHttp.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .build()

                chain.proceed(request)
            }
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://pro-api.coinmarketcap.com/")
                    // Add because it is type Observable, need an adapter for the data
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp.build())
                    .build()

            return retrofit.create(CoinMarketCapApi::class.java)
        }
    }
}
