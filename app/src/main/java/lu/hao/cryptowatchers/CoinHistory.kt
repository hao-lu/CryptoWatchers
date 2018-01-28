package lu.hao.cryptowatchers

import com.google.gson.annotations.SerializedName

data class CoinHistory(@SerializedName("market_cap") var marketCap: List<List<Float>>? = null,
                       var price: List<List<Float>>? = null,
                       var volume: List<List<Float>>? = null)