package com.firentis.bmiapp.bmi

class BmiForKgCm (
    override var mass: Double,
    override var height: Double

) : Bmi {

    override fun isCorrectMassVal(): Boolean {
        if (mass >= Bmi.kgMassMin && mass <= Bmi.kgMassMax)
            return true
        return false
    }

    override fun isCorrectHeightVal(): Boolean {
        if (height >= Bmi.cmHeightMin && height <= Bmi.cmHeightMax)
            return true
        return false
    }

    override fun count(): Double {
        return 10000 * mass / (height * height)
    }
}