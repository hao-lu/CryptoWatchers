package lu.hao.cryptowatchers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.databinding.ViewDataBinding
import android.databinding.DataBindingUtil
import android.view.LayoutInflater

class SearchResultsAdapter(var mCryptos: List<Coin>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private val TAG = "SearchResultsAdapter"

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
                .inflate(R.layout.search_result_row, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of the view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.crypto, mCryptos[position])
        holder.binding.executePendingBindings()
    }

    // Return the size of dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mCryptos.size
    }

}