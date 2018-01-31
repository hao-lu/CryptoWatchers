package lu.hao.cryptowatchers

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class Coin(val id: String,
                val name: String,
                val symbol: String,
                @SerializedName("price_usd") val priceUsd: Double,
                @SerializedName("24h_volume_usd") val volume: Double,
                @SerializedName("market_cap_usd") val marketCap: Double,
                @SerializedName("available_supply") val availableSupply: Double,
                @SerializedName("max_supply") val maxSupply: Double,
                @SerializedName("percent_change_1h") val percentChange1h: Double,
                @SerializedName("percent_change_24h") val percentChange24h: Double,
                @SerializedName("percent_change_7d") val percentChange7d: Double) : Parcelable {

    fun formatPrice(price: Double): String = if (price > 0.01) DecimalFormat("$###,##0.00").format(price) else DecimalFormat("$0.########").format(price)

    fun formatVolume(price: Double): String = DecimalFormat("$###,###").format(price)

    fun formatMarketCap(marketCap: Double): String = String.format("Market Cap: " + DecimalFormat("$###,###").format(marketCap))

    fun formatSupply(supply: Double): String = DecimalFormat("###,###").format(supply)

    // Some coins don't have a finite supply so return the available supply
    fun formatTotalSupply(supply: Double): String = if (supply == 0.0) formatSupply(this.availableSupply) else formatSupply(supply)

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(symbol)
        writeDouble(priceUsd)
        writeDouble(volume)
        writeDouble(marketCap)
        writeDouble(availableSupply)
        writeDouble(maxSupply)
        writeDouble(percentChange1h)
        writeDouble(percentChange24h)
        writeDouble(percentChange7d)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Coin> = object : Parcelable.Creator<Coin> {
            override fun createFromParcel(source: Parcel): Coin = Coin(source)
            override fun newArray(size: Int): Array<Coin?> = arrayOfNulls(size)
        }
    }
}


