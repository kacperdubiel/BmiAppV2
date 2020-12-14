package com.firentis.bmiapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.firentis.bmiapp.bmi.Bmi
import com.firentis.bmiapp.databinding.ActivityBmiInfoBinding
import java.io.IOException

class BmiInfoActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityBmiInfoBinding

    var sensor: Sensor? = null
    var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.bmi_info_title)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        val bmiValue = intent.getDoubleExtra("bmiValue", 0.0)
        Log.e("TAG", "Bmi VALUE: $bmiValue, (${bmiValue.javaClass.name})")
        val bmiCode = Bmi.getBmiCode(bmiValue)
        Log.e("TAG", "Bmi CODE: $bmiCode, (${bmiCode.javaClass.name})")
        val bmiColor = Bmi.getBmiColor(bmiCode)

        binding.bmiValTV.text = bmiValue.toString()
        binding.bmiValTV.setTextColor(ContextCompat.getColor(this@BmiInfoActivity, bmiColor))

        val bmiNameText: Int
        val bmiInfoText: Int

        when (bmiCode){
            Bmi.VERY_SEVERELY_UNDERWEIGHT_CODE -> {
                bmiNameText = R.string.bmi_very_severely_underweight
                bmiInfoText = R.string.bmi_very_severely_underweight_info
            }
            Bmi.SEVERELY_UNDERWEIGHT_CODE -> {
                bmiNameText = R.string.bmi_severely_underweight
                bmiInfoText = R.string.bmi_severely_underweight_info
            }
            Bmi.UNDERWEIGHT_CODE -> {
                bmiNameText = R.string.bmi_underweight
                bmiInfoText = R.string.bmi_underweight_info
            }
            Bmi.NORMAL_WEIGHT_CODE -> {
                bmiNameText = R.string.bmi_normal_weight
                bmiInfoText = R.string.bmi_normal_weight_info
            }
            Bmi.OVERWEIGHT_CODE -> {
                bmiNameText = R.string.bmi_overweight
                bmiInfoText = R.string.bmi_overweight_info
            }
            Bmi.MODERATELY_OBESE_CODE -> {
                bmiNameText = R.string.bmi_moderately_obese
                bmiInfoText = R.string.bmi_moderately_obese_info
            }
            Bmi.SEVERELY_OBESE_CODE -> {
                bmiNameText = R.string.bmi_severely_obese
                bmiInfoText = R.string.bmi_severely_obese_info
            }
            Bmi.VERY_SEVERELY_OBESE_CODE -> {
                bmiNameText = R.string.bmi_very_severely_obese
                bmiInfoText = R.string.bmi_very_severely_obese_info
            }
            else -> {
                bmiNameText = R.string.bmi_error_code
                bmiInfoText = R.string.bmi_error_code_info
            }
        }

        binding.bmiNameTV.text = getString(bmiNameText)
        binding.bmiNameTV.setTextColor(ContextCompat.getColor(this@BmiInfoActivity, bmiColor))

        binding.bmiInfoTV.text = getString(bmiInfoText)

        binding.backBTN.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event!!.values[0] < 10) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } catch (e: IOException) {

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}