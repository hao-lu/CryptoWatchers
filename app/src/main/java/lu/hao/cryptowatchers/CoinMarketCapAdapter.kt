package lu.hao.cryptowatchers

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.databinding.ViewDataBinding
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.widget.TextView

class CoinMarketCapAdapter(var mCryptos: List<Coin>) : RecyclerView.Adapter<CoinMarketCapAdapter.ViewHolder>() {

    private val TAG = "CoinMarketCapAdapter"

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding

        init {
            // Bind row view
            binding = DataBindingUtil.bind(v)
        }
    }

    // Create new view (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.crypto_info_row, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of the view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.crypto, mCryptos[position])
        val context = holder.itemView.context

//        val cardView = holder.itemView.findViewById<CardView>(R.id.card_info_row)
//        val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        val spacingInPixels = context.resources.getDimensionPixelSize(R.dimen.spacing)
//        if (position == 0) {
//
//            params.setMargins(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels)
//
//        }
//        else {
//            params.setMargins(spacingInPixels, 0, spacingInPixels, spacingInPixels)
//        }
//
//        cardView.layoutParams = params

        // Change the text color of percent based on positive or negative
        changePercentChangeColor(R.id.percent_change_1h, context, mCryptos[position].percentChange1h, holder)
        changePercentChangeColor(R.id.percent_change_24h, context, mCryptos[position].percentChange24h, holder)
        changePercentChangeColor(R.id.percent_change_7d, context, mCryptos[position].percentChange7d, holder)

        holder.itemView.setOnClickListener( {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra("Coin", mCryptos[position])
            context.startActivity(intent)
        } )

        holder.binding.executePendingBindings()
    }

    // Return the size of dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mCryptos.size
    }

    private fun changePercentChangeColor(id: Int, context: Context, percent: Double, holder: ViewHolder) {
        val percentage = holder.itemView.findViewById<TextView>(id) as TextView
        if (percent > 0) percentage.setTextColor(ContextCompat.getColor(context, R.color.colorUp))
        else percentage.setTextColor(ContextCompat.getColor(context, R.color.colorDown))
    }
}