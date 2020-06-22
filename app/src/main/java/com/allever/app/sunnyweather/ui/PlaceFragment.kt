package com.allever.app.sunnyweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.allever.app.sunnyweather.MainActivity
import com.allever.app.sunnyweather.R
import com.allever.app.sunnyweather.ui.adapter.PlaceAdapter
import com.allever.app.sunnyweather.ui.viewmodel.PlaceViewModel
import com.allever.lib.common.app.BaseFragment
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder
import com.allever.lib.common.ui.widget.recycler.ItemListener
import com.allever.lib.common.util.toast
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : BaseFragment() {

    private val mViewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var mAdapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is MainActivity && mViewModel.isPlaceSaved()) {
            val place = mViewModel.getSavedPlace()
            val location = place.location
            WeatherActivity.start(activity, location.lng, location.lat, place.name)
            activity?.finish()
            return
        }

        rvPlaceList.layoutManager = LinearLayoutManager(activity)
        mAdapter = PlaceAdapter(activity!!, R.layout.item_place, mViewModel.placeList)
        rvPlaceList.adapter = mAdapter
        etSearchPlace.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                mViewModel.searchPlaces(content)
            } else {
                rvPlaceList.visibility = View.GONE
                ivBg.visibility = View.VISIBLE
                mViewModel.placeList.clear()
                mAdapter.notifyDataSetChanged()
            }
        }

        mViewModel.placeLiveData.observe(this as LifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                rvPlaceList.visibility = View.VISIBLE
                ivBg.visibility = View.GONE
                mViewModel.placeList.clear()
                mViewModel.placeList.addAll(places)
                mAdapter.notifyDataSetChanged()
            } else {
                toast("未能查到任何地点")
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        mAdapter.mItemListener = object : ItemListener {
            override fun onItemClick(position: Int, holder: BaseViewHolder) {
                val place = mViewModel.placeList[position]
                val location = place.location

                if (activity is WeatherActivity) {
                    val weatherActivity = activity as WeatherActivity
                    weatherActivity.drawerLayout?.closeDrawers()
                    weatherActivity.viewModel.lng = location.lng
                    weatherActivity.viewModel.lat = location.lat
                    weatherActivity.viewModel.placeName = place.name
                    weatherActivity.refreshWeather()
                } else {
                    WeatherActivity.start(activity, location.lng, location.lat, place.name)
                    activity?.finish()
                }
                mViewModel.savePlace(place)
            }
        }
    }
}