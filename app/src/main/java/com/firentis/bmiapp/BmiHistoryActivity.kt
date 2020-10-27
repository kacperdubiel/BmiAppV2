package com.firentis.bmiapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bmi_history.*

class BmiHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)
        title = getString(R.string.bmi_history_title)

        val usingImperialUnits = intent.getBooleanExtra("usingImperialUnits", false)
        val bmiHistory: ArrayList<BmiMeasure> = intent.getSerializableExtra("bmiHistory") as ArrayList<BmiMeasure>

        bmiMeasuresRV.adapter = BmiHistoryAdapter(bmiHistory, usingImperialUnits)
        bmiMeasuresRV.layoutManager = LinearLayoutManager(this)
        bmiMeasuresRV.setHasFixedSize(true)

        val backBtn: Button = findViewById(R.id.backBTN2)
        backBtn.setOnClickListener {
            finish()
        }
    }
}