package lu.hao.cryptowatchers.view.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import lu.hao.cryptowatchers.BR
import lu.hao.cryptowatchers.view.activity.CoinDetailsActivity
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.model.data.Coin

class SearchResultsAdapter(var mCoins: MutableList<Coin>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private val TAG = "SearchResultsAdapter"

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding

        init {
            // Bind row view
            binding = DataBindingUtil.bind(v)!!
        }
    }

    // Create new view (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_row, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of the view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.crypto, mCoins[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener( {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra("Coin", mCoins[position])
            context.startActivity(intent)
        } )
        holder.binding.executePendingBindings()
    }

    // Return the size of dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mCoins.size
    }

}