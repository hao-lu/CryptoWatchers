package lu.hao.cryptowatchers.view.adapter

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

private val BASE_URL = "https://static.coincap.io/assets/icons/%s@2x.png"

@BindingAdapter("bind:imageUrl")
fun loadImage(imageView: ImageView, url: String) {
    if (!url.equals("")) {
        val link = String.format(BASE_URL, url.toLowerCase())
        Picasso.get().load(link).into(imageView)
    }
}

