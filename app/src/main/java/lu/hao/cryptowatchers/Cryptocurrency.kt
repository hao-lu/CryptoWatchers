package lu.hao.cryptowatchers

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class Cryptocurrency (val id: String,
                           val name: String,
                           val symbol: String,
                           @SerializedName("price_usd") val priceUsd: Double,
                           @SerializedName("market_cap_usd") val marketCap: Double,
                           @SerializedName("percent_change_1h") val percentChange1h: Double,
                           @SerializedName("percent_change_24h") val percentChange24h: Double,
                           @SerializedName("percent_change_7d") val percentChange7d: Double) {

    fun formatPrice(price: Double): String = DecimalFormat("$0.00").format(price)

    fun formatMarketCap(marketCap: Double): String = String.format("Market Cap: " + DecimalFormat("$000,000").format(marketCap))

//    fun formatPercent1h(percent: String): String = String.format(" $percent%%")

//    fun formatPercent24h(percent: String): String = String.format(" $percent%%")

//    fun formatPercent7d(percent: String): String = String.format(" $percent%%")

}


