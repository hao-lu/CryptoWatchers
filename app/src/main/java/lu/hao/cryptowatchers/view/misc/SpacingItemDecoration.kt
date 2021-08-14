package lu.hao.cryptowatchers.view.misc

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class SpacingItemDecoration : RecyclerView.ItemDecoration() {

    // 1dp = 3 px
    private val space = 24

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space - 4
        } else {
            // Remove the extra spacing from attribute: cardUseCompatPadding
            outRect.top = -space
        }
    }
}