package com.firentis.bmiapp

import java.io.Serializable

data class BmiMeasure (val bmiValue: Double, val bmiCode: Int, val mass: Double, val height: Double, val date: String) : Serializable
