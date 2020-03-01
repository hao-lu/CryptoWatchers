package lu.hao.cryptowatchers.view.adapter

import com.robinhood.spark.SparkAdapter
import lu.hao.cryptowatchers.model.data.CoinHistory
import lu.hao.cryptowatchers.model.data.Price

class CoinHistoryAdapter(list: List<Price>) : SparkAdapter() {
    private var mData = mutableListOf<Pair<Float, Float>>()
    private val TAG = "CoinHistoryAdapter"

    init {
//        for (i in 0 until history.price!!.size) {
//            val p = Pair(history.price!![i][0], history.price!![i][1])
//            mData.add(p)
//        }
        list.forEach {
            mData.add(Pair(it.time.toFloat(), it.priceUsd.toFloat()))
        }
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(index: Int): Any {
        return mData[index]
    }

    override fun getY(index: Int): Float {
        return mData[index].second
    }
}