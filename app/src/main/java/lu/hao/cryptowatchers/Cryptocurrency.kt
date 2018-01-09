package lu.hao.cryptowatchers

import com.google.gson.annotations.SerializedName

data class Cryptocurrency (val id: String,
                           val name: String,
                           val symbol: String,
                           @SerializedName("price_usd") val priceUsd: String,
                           @SerializedName("percent_change_24h") val percentChange24h: String,
                           @SerializedName("percent_change_7d") val percentChange7d: String)

//data class Cryptocurrency(var id: String,
//                          var name: String,
//                          var symbol: String,
//                          var rank: String,
//                          @SerializedName("price_usd") var priceUsd: Double,
//                          @SerializedName("price_btc") var priceBtc: Double,
//                          @SerializedName("24h_volume_usd") var volumeUsd24h: Double,
//                          @SerializedName("market_cap_usd") var marketCapUsd: Double,
//                          @SerializedName("available_supply") var availableSupply: Double,
//                          @SerializedName("total_supply") var totalSupply: Double,
//                          @SerializedName("max_supply") var maxSupply: Double,
//                          @SerializedName("percent_change_1h") var percentChange1h: Double,
//                          @SerializedName("percent_change_24h") var percentChange24h: Double,
//                          @SerializedName("percent_change_7d") var percentChange7d: Double,
//                          @SerializedName("last_updated") var lastUpdated: Double) : Serializable
//
