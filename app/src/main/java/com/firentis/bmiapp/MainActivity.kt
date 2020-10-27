package com.firentis.bmiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.firentis.bmiapp.bmi.Bmi
import com.firentis.bmiapp.bmi.BmiForKgCm
import com.firentis.bmiapp.bmi.BmiForLbIn
import com.firentis.bmiapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var usingImperialUnits: Boolean = false
    private var bmiHistory: ArrayList<BmiMeasure> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bmiTV.setOnClickListener {
            val bmiValue = binding.bmiTV.text.toString().toDouble()

            val bmiCode = Bmi.getBmiCode(bmiValue)
            if(bmiCode != Bmi.ERROR_WEIGHT_CODE) {
                val intent = Intent(this, BmiInfoActivity::class.java)
                intent.putExtra("bmiValue", bmiValue)
                startActivityForResult(intent, 1)
            }
        }

        loadBmiHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        
        val bmiValue = binding.bmiTV.text.toString().toDouble()
        outState.putDouble("bmiValue", bmiValue)

        outState.putBoolean("usingImperialUnits", usingImperialUnits)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val bmiValue = savedInstanceState.getDouble("bmiValue")
        binding.bmiTV.text = bmiValue.toString()
        val bmiCode = Bmi.getBmiCode(bmiValue)
        if(bmiCode != Bmi.ERROR_WEIGHT_CODE) {
            val bmiColor = Bmi.getBmiColor(bmiCode)
            binding.bmiTV.setTextColor(ContextCompat.getColor(this@MainActivity, bmiColor))
        }

        usingImperialUnits = savedInstanceState.getBoolean("usingImperialUnits")
        if(usingImperialUnits) {
            binding.massTV.text = getString(R.string.mass_lb)
            binding.heightTV.text = getString(R.string.height_in)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        menu.findItem(R.id.to_imperial_unit).isChecked = usingImperialUnits
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Changing units type
            R.id.to_imperial_unit -> {
                if(!item.isChecked)
                    changeToImperial()
                else
                    changeToMetric()

                item.isChecked = !item.isChecked
                true
            }
            R.id.bmi_history -> {
                val intent = Intent(this, BmiHistoryActivity::class.java)
                intent.putExtra("usingImperialUnits", usingImperialUnits)
                intent.putExtra("bmiHistory", bmiHistory as Serializable)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeToImperial() {
        binding.apply {
            massTV.text = getString(R.string.mass_lb)
            if(!massET.text.isBlank()) {
                val massText = massET.text.toString().replace(",",".")
                val newMass = Bmi.convKgToLb(massText.toDouble())
                massET.setText(DecimalFormat("#.00").format(newMass).replace(",","."))
            }

            heightTV.text = getString(R.string.height_in)
            if(!heightET.text.isBlank()) {
                val heightText = heightET.text.toString().replace(",",".")
                val newHeight = Bmi.convCmToIn(heightText.toDouble())
                heightET.setText(DecimalFormat("#.00").format(newHeight).replace(",","."))
            }

            usingImperialUnits = true
        }
    }

    private fun changeToMetric() {
        binding.apply {
            massTV.text = getString(R.string.mass_kg)
            if(!massET.text.isBlank()) {
                val massText = massET.text.toString().replace(",",".")
                val newMass = Bmi.convLbToKg(massText.toDouble())
                massET.setText(DecimalFormat("#.00").format(newMass).replace(",","."))
            }

            heightTV.text = getString(R.string.height_cm)
            if(!heightET.text.isBlank()) {
                val heightText = heightET.text.toString().replace(",",".")
                val newHeight = Bmi.convInToCm(heightText.toDouble())
                heightET.setText(DecimalFormat("#.00").format(newHeight).replace(",","."))
            }

            usingImperialUnits = false
        }
    }

    fun countBmi(view: View) {
        binding.apply {
            var errorsOccurred = false

            val bmi: Bmi = if(!usingImperialUnits) BmiForKgCm(0.0, 0.0)
                           else BmiForLbIn(0.0, 0.0)

            // --- MASS ---
            if(massET.text.isBlank()) {
                massET.error = getString(R.string.empty_mass)
                errorsOccurred = true
            }
            else {
                // User mass input
                bmi.mass = massET.text.toString().replace(",",".").toDouble()

                // Checking if the user mass input is correct
                if(!bmi.isCorrectMassVal()) {
                    massET.error = getString(R.string.incorrect_mass)
                    errorsOccurred = true
                }
            }

            // --- HEIGHT ---
            if(heightET.text.isBlank()) {
                heightET.error = getString(R.string.empty_height)
                errorsOccurred = true
            }
            else {
                // User height input
                bmi.height = heightET.text.toString().replace(",",".").toDouble()

                // Checking if the user height input is correct
                if(!bmi.isCorrectHeightVal()) {
                    heightET.error = getString(R.string.incorrect_height)
                    errorsOccurred = true
                }
            }

            if(!errorsOccurred) {
                val bmiValue = bmi.count()
                bmiTV.text = DecimalFormat("#.00").format(bmiValue).replace(",",".")

                val bmiCode = Bmi.getBmiCode(bmiValue)
                val bmiColor = Bmi.getBmiColor(bmiCode)
                bmiTV.setTextColor(ContextCompat.getColor(this@MainActivity, bmiColor))

                if(usingImperialUnits)
                    addMeasureToHistory(bmiValue, bmiCode, Bmi.convLbToKg(bmi.mass), Bmi.convInToCm(bmi.height))
                else
                    addMeasureToHistory(bmiValue, bmiCode, bmi.mass, bmi.height)

                saveBmiHistory()
            }
        }
    }

    private fun addMeasureToHistory(bmiValue: Double, bmiCode: Int, mass: Double, height: Double) {
        val dateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yy HH:mm")
        val dateString: String = formatter.format(dateTime)

        val newBmiMeasure = BmiMeasure(bmiValue, bmiCode, mass, height, dateString)
        if(bmiHistory.size > 0 && bmiHistory.size >= Bmi.bmiHistorySize)
            bmiHistory.removeAt(bmiHistory.size - 1)
        bmiHistory.add(0, newBmiMeasure)
    }

    private fun saveBmiHistory() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()

        val bmiHistoryJson: String = gson.toJson(bmiHistory)
        editor.putString("bmiHistory", bmiHistoryJson)
        editor.apply()

    }

    private fun loadBmiHistory(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<ArrayList<BmiMeasure>>() {}.type

        val bmiHistoryJson: String? = sharedPreferences.getString("bmiHistory", "")

        val bmiHistoryArray: ArrayList<BmiMeasure>
        bmiHistoryArray = if (bmiHistoryJson != "") gson.fromJson(bmiHistoryJson, type)
                          else ArrayList()
        bmiHistory = bmiHistoryArray
        if(bmiHistory.size > Bmi.bmiHistorySize && Bmi.bmiHistorySize > 0){
            while(bmiHistory.size > Bmi.bmiHistorySize)
                bmiHistory.removeAt(bmiHistory.size - 1)
            saveBmiHistory()
        }
    }
}