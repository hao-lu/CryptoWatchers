package lu.hao.cryptowatchers

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

class DataBindingAdapters {
    @BindingAdapter( "bind:imageUrl" )
    fun loadImage(imageView: ImageView, url: String) {
        if (!url.equals("")) {
            Picasso.with(imageView.getContext()).load(url).into(imageView)

        }
    }

}
