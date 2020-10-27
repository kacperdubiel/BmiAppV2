package com.firentis.bmiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firentis.bmiapp.bmi.Bmi
import kotlinx.android.synthetic.main.bmi_measure.view.*
import java.text.DecimalFormat


class BmiHistoryAdapter(
    private val bmiHistory: ArrayList<BmiMeasure>,
    private val usingImperialUnits: Boolean
) : RecyclerView.Adapter<BmiHistoryAdapter.BmiHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bmi_measure, parent, false)
        return BmiHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BmiHistoryViewHolder, position: Int) {
        val context = holder.itemView.context
        val measure = bmiHistory[position]

        holder.bmiValTV.text = DecimalFormat("#.00").format(measure.bmiValue)
        val bmiColor = Bmi.getBmiColor(measure.bmiCode)
        holder.bmiValTV.setTextColor(ContextCompat.getColor(context, bmiColor))

        val massVal: Double
        val massValToString: String
        val massText: String

        val heightVal: Double
        val heightValToString: String
        val heightText: String

        if(usingImperialUnits){
            massVal = Bmi.convKgToLb(measure.mass)
            massValToString = DecimalFormat("#.00").format(massVal).replace(",",".")
            massText = context.getString(R.string.mass_lb_with_val, massValToString)

            heightVal = Bmi.convCmToIn(measure.height)
            heightValToString = DecimalFormat("#.00").format(heightVal).replace(",",".")
            heightText = context.getString(R.string.height_in_with_val, heightValToString)
        }
        else{
            massValToString = DecimalFormat("#.00").format(measure.mass).replace(",",".")
            massText = context.getString(R.string.mass_kg_with_val, massValToString)

            heightValToString = DecimalFormat("#.00").format(measure.height).replace(",",".")
            heightText = context.getString(R.string.height_cm_with_val, heightValToString)
        }

        holder.massValTV.text = massText
        holder.heightValTV.text = heightText

        holder.dateTV.text = measure.date
    }

    override fun getItemCount() = bmiHistory.size

    class BmiHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bmiValTV: TextView = itemView.bmiValTV
        val massValTV: TextView = itemView.massValTV
        val heightValTV: TextView = itemView.heightValTV
        val dateTV: TextView = itemView.dateTV
    }
}
