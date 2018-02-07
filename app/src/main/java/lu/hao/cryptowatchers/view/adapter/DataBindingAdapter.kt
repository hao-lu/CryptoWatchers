package lu.hao.cryptowatchers.view.adapter

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

private val BASE_URL = "https://files.coinmarketcap.com/static/img/coins/32x32/"

@BindingAdapter("bind:imageUrl")
fun loadImage(imageView: ImageView, url: String) {
    if (!url.equals("")) {
        val link = BASE_URL + url + ".png"
        Picasso.with(imageView.context).load(link).into(imageView)
    }
}

