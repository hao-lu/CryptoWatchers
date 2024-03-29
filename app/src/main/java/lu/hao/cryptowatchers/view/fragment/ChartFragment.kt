package lu.hao.cryptowatchers.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.robinhood.spark.SparkView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_chart.*
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin
import lu.hao.cryptowatchers.model.api.CoinCapApi
import lu.hao.cryptowatchers.model.data.CoinHistory
import lu.hao.cryptowatchers.view.adapter.CoinHistoryAdapter
import lu.hao.cryptowatchers.viewmodel.ChartViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {

    private val TAG = "ChartFragment"
    private val mViewModel = ChartViewModel()
    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_chart, container, false)
        val period = arguments?.getString("period")
        val coin = arguments?.getParcelable<Coin>("coin")

        val observable = CoinCapApi.create().getHistoryObservable("bitcoin", "d1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val disposable = observable.subscribe(
                { history ->
                    spark_view.adapter = CoinHistoryAdapter(history.data)
                    // Turn off progress bar and show chart fragment
                    activity?.findViewById<ProgressBar>(R.id.chart_progress_bar)?.visibility = ProgressBar.GONE
                    activity?.findViewById<FrameLayout>(R.id.chart_container)?.visibility = FrameLayout.VISIBLE

                    // Calculate the change
                    val oldPrice = spark_view.adapter.getY(spark_view.adapter.count - 1)
                    val latestPrice = spark_view.adapter.getY(0)
                    val difference = Math.abs(oldPrice - latestPrice).toDouble()
                    val percentChange = ((oldPrice - latestPrice) / oldPrice) * 100
                    val formatChange = DecimalFormat("#0.00'%'").format(percentChange)
                    val change = "$formatChange (${coin?.formatPrice(difference)})"

                    val oldTextColor = scrub_x_data.textColors
                    if (percentChange < 0) scrub_x_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDown))
                    else scrub_x_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorUp))

                    // Initially set the price and percentChange
                    scrub_y_data.text = coin?.formatPrice(coin?.priceUsd)
                    scrub_x_data.text = change

                    spark_view.fillType = SparkView.FillType.DOWN
                    spark_view.setScrubListener(
                            { value ->
                        if (value == null) {
                            scrub_y_data.text = coin?.formatPrice(coin.priceUsd)
                            scrub_x_data.text = change

                            if (percentChange < 0) scrub_x_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDown))
                            else scrub_x_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorUp))

                        } else {
                            scrub_x_data.setTextColor(oldTextColor)
                            val pair: Pair<Float, Float> = value as Pair<Float, Float>
                            val sdf = SimpleDateFormat("MM-dd-yy hh:mm a", Locale.US)
                            scrub_y_data.text = coin?.formatPrice(pair.second.toDouble())
                            scrub_x_data.text = sdf.format(pair.first)
                        }
                    })

                },
                {
                    e-> Log.d(TAG, "Error: " + e.message)
                    if (e.message == "Null is not a valid element") {
                        // May need to check the view is still in view
                        activity?.findViewById<ProgressBar>(R.id.chart_progress_bar)?.visibility = ProgressBar.GONE
                        activity?.findViewById<TextView>(R.id.no_data_available)?.visibility = TextView.VISIBLE
                    }
                },
                { Log.d(TAG, "onComplete") }
        )

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCompositeDisposable.clear()
    }
}