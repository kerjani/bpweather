package com.weather.bp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.weather.bp.R
import com.weather.bp.data.models.typeconverter.Iso8601TypeConverter
import com.weather.bp.databinding.MainFragmentBinding
import com.weather.bp.util.Constants.BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var dateFormat: DateFormat

    @Inject
    lateinit var dateConverter: Iso8601TypeConverter

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.latestWeatherForecast.observe(viewLifecycleOwner, Observer {
            it?.let { weather ->
                viewModel.temperature.value = getString(R.string.temperature, weather.the_temp)
                viewModel.dataAge.value =
                    getString(
                        R.string.updated,
                        dateFormat.format(Date(dateConverter.stringToDateTimeInMillis(weather.created)))
                    )
                viewModel.weatherName.value = weather.weather_state_name
                Glide.with(this)
                    .load("${BASE_URL}static/img/weather/png/${weather.weather_state_abbr}.png") // TODO pixel perfect image downloading
                    .into(binding.weatherImage)
            }
            viewModel.isLoading.value = false
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Snackbar.make(view, error, LENGTH_LONG).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getWeather()
    }

}