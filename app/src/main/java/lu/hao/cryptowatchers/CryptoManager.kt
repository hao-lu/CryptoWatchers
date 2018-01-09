package lu.hao.cryptowatchers

import android.util.Log
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback

class CryptoManager {

    private val TAG = "CryptoManager"
//    private var cryptos: List<Cryptocurrency>? = null

    fun getCryptocurrnecies(): List<Cryptocurrency>? {
        Log.d(TAG, "BEFORE CALL RESPONSE")
        val apiService = CryptoInterface.create()
        val callResponse = apiService.getTickerLimit("10")


        callResponse.enqueue(object : Callback<List<Cryptocurrency>> {
            override fun onResponse(call: Call<List<Cryptocurrency>>,
                           response: retrofit2.Response<List<Cryptocurrency>>) {
                if (response.isSuccessful) {
                    val cryptosResponse = response.body()
                    Log.d(TAG, cryptosResponse!![0].name)
                    Log.d(TAG, "onResponse")
//                    cryptos = cryptosResponse
                }
                else {
                    Log.d(TAG, "Response is not successful")
                }
            }

            override fun onFailure(call: Call<List<Cryptocurrency>>, t: Throwable) {
                Log.d(TAG, t.stackTrace.toString())
                Log.d(TAG, "onFailure")
            }
        })

        return null

    }


    fun getCryptos(): Observable<List<Cryptocurrency>> {
        return Observable.create {
            subscriber ->

            val apiService = CryptoInterface.create()
            val callResponse = apiService.getTickerLimit("10")
            val response = callResponse.execute()
            if (response.isSuccessful) {
                Log.d(TAG, "onResponse")
                val cryptos = response.body()
                subscriber.onNext(cryptos!!)
                subscriber.onComplete()
            }
            else {
                subscriber.onError(Throwable(response.message()))
                Log.d(TAG, "not successful")
            }

            Log.d(TAG, "getCryptos()")

        }

    }

}
