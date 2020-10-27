package com.firentis.bmiapp.bmi

import com.firentis.bmiapp.R

interface Bmi {
    var mass: Double
    var height: Double

    fun count(): Double
    fun isCorrectMassVal(): Boolean
    fun isCorrectHeightVal(): Boolean

    companion object {
        const val bmiHistorySize = 15

        // -- Values range --
        const val kgMassMin = 20.0
        const val kgMassMax = 300.0
        const val cmHeightMin = 30.0
        const val cmHeightMax = 250.0

        // -- Conversion --

        private const val KG_TO_LB_RATIO = 2.2046
        private const val LB_TO_KG_RATIO = 0.4536

        private const val CM_TO_IN_RATIO = 0.3937
        private const val IN_TO_CM_RATIO = 2.54

        fun convKgToLb(value: Double): Double {
            return value * KG_TO_LB_RATIO
        }

        fun convLbToKg(value: Double): Double {
            return value * LB_TO_KG_RATIO
        }

        fun convCmToIn(value: Double): Double {
            return value * CM_TO_IN_RATIO
        }

        fun convInToCm(value: Double): Double {
            return value * IN_TO_CM_RATIO
        }

        // -- Bmi Values

        const val ERROR_WEIGHT_CODE = -1
        const val VERY_SEVERELY_UNDERWEIGHT_CODE = 0
        const val SEVERELY_UNDERWEIGHT_CODE = 1
        const val UNDERWEIGHT_CODE = 2
        const val NORMAL_WEIGHT_CODE = 3
        const val OVERWEIGHT_CODE = 4
        const val MODERATELY_OBESE_CODE = 5
        const val SEVERELY_OBESE_CODE = 6
        const val VERY_SEVERELY_OBESE_CODE = 7

        fun getBmiCode(bmiVal: Double): Int {
            return when (bmiVal) {
                in Double.MIN_VALUE .. 16.0 -> VERY_SEVERELY_UNDERWEIGHT_CODE
                in 16.0 .. 16.99 -> SEVERELY_UNDERWEIGHT_CODE
                in 16.99 .. 18.49 -> UNDERWEIGHT_CODE
                in 18.49 .. 25.0 -> NORMAL_WEIGHT_CODE
                in 25.0 .. 30.0 -> OVERWEIGHT_CODE
                in 30.0 .. 35.0 -> MODERATELY_OBESE_CODE
                in 35.0 .. 40.0 -> SEVERELY_OBESE_CODE
                in 40.0 .. Double.MAX_VALUE -> VERY_SEVERELY_OBESE_CODE
                else -> ERROR_WEIGHT_CODE
            }
        }

        fun getBmiColor(bmiCode: Int): Int {
            return when (bmiCode) {
                VERY_SEVERELY_UNDERWEIGHT_CODE -> R.color.verySeverelyUnderweight
                SEVERELY_UNDERWEIGHT_CODE -> R.color.severelyUnderweight
                UNDERWEIGHT_CODE -> R.color.underweight
                NORMAL_WEIGHT_CODE -> R.color.normalWeight
                OVERWEIGHT_CODE -> R.color.overweight
                MODERATELY_OBESE_CODE -> R.color.moderatelyObese
                SEVERELY_OBESE_CODE -> R.color.severelyObese
                VERY_SEVERELY_OBESE_CODE -> R.color.verySeverelyObese
                else -> R.color.defaultFontColor
            }
        }

    }

}