package com.allever.app.sunnyweather.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.allever.app.sunnyweather.R
import com.allever.app.sunnyweather.ui.viewmodel.WeatherViewModel
import com.allever.lib.common.app.BaseActivity
import kotlinx.android.synthetic.main.activity_weather.*

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

    val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        if (viewModel.lng.isEmpty()) {
            viewModel.lng = intent.getStringExtra("lng") ?: ""
        }

        if (viewModel.lat.isEmpty()) {
            viewModel.lat = intent.getStringExtra("lat") ?: ""
        }

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
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

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

        })

    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.lng, viewModel.lat)
        swipeRefreshLayout.isRefreshing = true
    }

}