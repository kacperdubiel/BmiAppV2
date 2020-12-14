package com.firentis.bmiapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.firentis.bmiapp.database.Measure
import kotlinx.android.synthetic.main.activity_bmi_history.*
import java.io.IOException

class BmiHistoryActivity : AppCompatActivity(), SensorEventListener {
    var sensor: Sensor? = null
    var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)
        title = getString(R.string.bmi_history_title)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        val usingImperialUnits = intent.getBooleanExtra("usingImperialUnits", false)
        val bmiHistory: List<Measure> = intent.getSerializableExtra("bmiHistory") as List<Measure>

        bmiMeasuresRV.adapter = BmiHistoryAdapter(bmiHistory, usingImperialUnits)
        bmiMeasuresRV.layoutManager = LinearLayoutManager(this)
        bmiMeasuresRV.setHasFixedSize(true)

        val backBtn: Button = findViewById(R.id.backBTN2)
        backBtn.setOnClickListener {
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