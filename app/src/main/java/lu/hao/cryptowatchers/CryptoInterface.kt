package lu.hao.cryptowatchers

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoInterface {
    @GET("v1/ticker/")
    fun getTicker() : Call<CryptoResponse>

    @GET("v1/ticker/")
    // Using Call<List<Cryptocurrency>> rather than Call<CryptoResponse> because API is returning a JSONArray rather than JSONObject
    fun getTickerLimit(@Query("limit") limit: String) : Call<List<Cryptocurrency>>

    companion object Factory {
        fun create(): CryptoInterface {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.coinmarketcap.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(CryptoInterface::class.java)
        }

    }
}
