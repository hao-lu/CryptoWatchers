package lu.hao.cryptowatchers

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit initialization
class RestApi {

    private val TAG = "RestApi"
    private val cryptoInterface: CryptoInterface

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        cryptoInterface = retrofit.create(CryptoInterface::class.java)
    }

    // Consume the API and request the cryptos
    fun getCryptos(): Call<List<Cryptocurrency>> {
        return cryptoInterface.getTickerLimit("10")
    }

}