package lu.hao.cryptowatchers

import com.google.gson.annotations.SerializedName

data class CoinHistory(@SerializedName("market_cap") var marketCap: List<List<Int>>? = null,
                       var price: List<List<Int>>? = null,
                       var volume: List<List<Int>>? = null)