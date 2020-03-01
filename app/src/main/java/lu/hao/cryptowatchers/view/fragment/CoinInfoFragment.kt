package lu.hao.cryptowatchers.view.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lu.hao.cryptowatchers.BR
import lu.hao.cryptowatchers.R
import lu.hao.cryptowatchers.databinding.FragmentCoinInfoBinding
import lu.hao.cryptowatchers.model.data.Coin

class CoinInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val coin = arguments.getParcelable<Coin>("coin")
        val binding = DataBindingUtil.inflate<FragmentCoinInfoBinding>(inflater, R.layout.fragment_coin_info, container, false)
        binding.setVariable(BR.coin, coin)
        binding.executePendingBindings()

        return binding.root
    }
}
