package lu.hao.cryptowatchers

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CryptoFragment : Fragment() {

    private val TAG = "CryptoFragment"
    private val cryptoManager by lazy { CryptoManager() }


    companion object {
        fun newInstance(): CryptoFragment {
            return CryptoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.cryptos_fragment, container,
                false)
        val activity = activity
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val cryptoManager = CryptoManager()
        val cryptos = cryptoManager.getCryptos()

//        recyclerView.adapter = CryptoAdapter(cryptos!!)

        if (cryptos == null)
            Log.d(TAG, "null")
        else
            Log.d(TAG, "not null")

        return view

    }

}