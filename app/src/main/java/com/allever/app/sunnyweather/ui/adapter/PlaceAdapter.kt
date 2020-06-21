package com.allever.app.sunnyweather.ui.adapter

import android.content.Context
import com.allever.app.sunnyweather.R
import com.allever.app.sunnyweather.function.model.Place
import com.allever.lib.common.ui.widget.recycler.BaseRecyclerViewAdapter
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder

class PlaceAdapter(context: Context, resId: Int, data: MutableList<Place>) :
    BaseRecyclerViewAdapter<Place>(context, resId, data) {

    override fun bindHolder(holder: BaseViewHolder, position: Int, item: Place) {
        holder.setText(R.id.tvPlaceName, item.name)
        holder.setText(R.id.tvPlaceAddress, item.address)
    }
}