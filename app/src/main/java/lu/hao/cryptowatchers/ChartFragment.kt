package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import kotlinx.android.synthetic.main.activity_coin_details.*
import kotlinx.android.synthetic.main.fragment_chart.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {

    private val TAG = "ChartFragment"

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_chart, container, false)
        val period = arguments.getString("period")
        val coin = arguments.getParcelable<Coin>("coin")

        val observable = CoinCapApi.create().getHistoryObservable(period, coin.symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val disposable = observable.subscribe(
                { history ->
                    spark_view.adapter = CoinHistoryAdapter(history)

                    activity.findViewById<ProgressBar>(R.id.chart_progress_bar).visibility = ProgressBar.GONE
                    activity.findViewById<FrameLayout>(R.id.chart_container).visibility = FrameLayout.VISIBLE

                    spark_view.fillType = SparkView.FillType.DOWN
                    // Initially set the price and percentChange
                    scrub_y_data.text = coin.formatPrice(coin.priceUsd)

                    val oldPrice = spark_view.adapter.getY(spark_view.adapter.count - 1)
                    val latestPrice = spark_view.adapter.getY(0)
                    val difference = Math.abs(oldPrice - latestPrice).toDouble()
                    val change = "${coin.percentChange24h}% (${coin.formatPrice(difference)})"
                    scrub_x_data.text = change
//                    scrub_x_data.text = coin.percentChange1h.toString()

                    spark_view.setScrubListener(
                            { value ->
                        if (value == null) {
                            scrub_y_data.text = coin.formatPrice(coin.priceUsd)
//                            scrub_x_data.text = coin.percentChange1h.toString()
                            scrub_x_data.text = change
                        } else {
                            val pair: Pair<Float, Float> = value as Pair<Float, Float>
                            val sdf = SimpleDateFormat("MM-dd-yy hh:mm a", Locale.US)
                            scrub_y_data.text = coin.formatPrice(pair.second.toDouble())
                            scrub_x_data.text = sdf.format(pair.first)
                        }
                    })

                },
                { e-> Log.d(TAG, "Error: " + e.message) },
                { Log.d(TAG, "onComplete") }
        )

        return rootView
    }

    private class CoinHistoryAdapter(history: CoinHistory) : SparkAdapter() {
        private val xData: FloatArray
        private val yData: FloatArray
        private var marketCapHistory: List<List<Float>>
        private val TAG = "CoinHistoryAdapter"

        init {
            xData = FloatArray(history.price!!.size)
            yData = FloatArray(history.price!!.size)
            marketCapHistory = history.price!!
            fill()
        }

        private fun fill() {
            for (i in 0 until marketCapHistory.size) {
                xData[i] = marketCapHistory[i][0]
                yData[i] = marketCapHistory[i][1]
            }
        }

        override fun getCount(): Int {
            return yData.size
        }

        override fun getItem(index: Int): Any {
            return Pair(xData[index], yData[index])
        }

        override fun getY(index: Int): Float {
            return yData[index]
        }
    }

}