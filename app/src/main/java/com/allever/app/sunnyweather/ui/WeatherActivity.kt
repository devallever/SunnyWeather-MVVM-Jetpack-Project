package com.allever.app.sunnyweather.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.allever.app.sunnyweather.R
import com.allever.app.sunnyweather.ui.viewmodel.WeatherViewModel
import com.allever.lib.common.app.BaseActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlin.math.ln

class WeatherActivity : BaseActivity() {

    companion object {
        fun start(context: Context?, lng: String, lat: String, place: String) {
            val intent = Intent(context, WeatherActivity::class.java)
            intent.putExtra("lng", lng)
            intent.putExtra("lat", lat)
            intent.putExtra("place", place)
            context?.startActivity(intent)
        }
    }

    private val mViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        if (mViewModel.lng.isEmpty()) {
            mViewModel.lng = intent.getStringExtra("lng") ?: ""
        }

        if (mViewModel.lat.isEmpty()) {
            mViewModel.lat = intent.getStringExtra("lat") ?: ""
        }

        if (mViewModel.placeName.isEmpty()) {
            mViewModel.placeName = intent.getStringExtra("place") ?: ""
        }

        mViewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                tvWeatherResult.text = weather.toString()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                tvWeatherResult.text = result.exceptionOrNull()?.message
            }
            swipeRefreshLayout.isRefreshing = false
        })

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()

        swipeRefreshLayout.setOnRefreshListener {
            refreshWeather()
        }
    }

    private fun refreshWeather() {
        mViewModel.refreshWeather(mViewModel.lng, mViewModel.lat)
        swipeRefreshLayout.isRefreshing = true
    }

}