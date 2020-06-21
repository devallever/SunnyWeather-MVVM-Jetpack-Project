package com.allever.app.sunnyweather.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.allever.app.sunnyweather.R
import com.allever.app.sunnyweather.ui.adapter.PlaceAdapter
import com.allever.app.sunnyweather.ui.viewmodel.PlaceViewModel
import com.allever.lib.common.app.BaseFragment
import com.allever.lib.common.util.toast
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : BaseFragment() {

    val viewModel by lazy {
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
        rvPlaceList.layoutManager = LinearLayoutManager(activity)
        mAdapter = PlaceAdapter(activity!!, R.layout.item_place, viewModel.placeList)
        rvPlaceList.adapter = mAdapter
        etSearchPlace.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                rvPlaceList.visibility = View.GONE
                ivBg.visibility = View.VISIBLE
                viewModel.placeList.clear()
                mAdapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(this as LifecycleOwner, Observer {result ->
            val places = result.getOrNull()
            if (places != null) {
                rvPlaceList.visibility = View.VISIBLE
                ivBg.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                mAdapter.notifyDataSetChanged()
            } else {
                toast("未能查到任何地点")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}