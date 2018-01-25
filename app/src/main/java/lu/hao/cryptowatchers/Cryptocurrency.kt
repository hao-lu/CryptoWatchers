package lu.hao.cryptowatchers

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class Cryptocurrency(val id: String,
                          val name: String,
                          val symbol: String,
                          @SerializedName("price_usd") val priceUsd: Double,
                          @SerializedName("market_cap_usd") val marketCap: Double,
                          @SerializedName("percent_change_1h") val percentChange1h: Double,
                          @SerializedName("percent_change_24h") val percentChange24h: Double,
                          @SerializedName("percent_change_7d") val percentChange7d: Double) : Parcelable {
    fun formatPrice(price: Double): String = DecimalFormat("$0.00").format(price)

    fun formatMarketCap(marketCap: Double): String = String.format("Market Cap: " + DecimalFormat("$000,000").format(marketCap))

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
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
        writeDouble(marketCap)
        writeDouble(percentChange1h)
        writeDouble(percentChange24h)
        writeDouble(percentChange7d)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cryptocurrency> = object : Parcelable.Creator<Cryptocurrency> {
            override fun createFromParcel(source: Parcel): Cryptocurrency = Cryptocurrency(source)
            override fun newArray(size: Int): Array<Cryptocurrency?> = arrayOfNulls(size)
        }
    }
}


