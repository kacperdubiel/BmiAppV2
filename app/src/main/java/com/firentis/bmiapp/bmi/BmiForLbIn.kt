package com.firentis.bmiapp.bmi

class BmiForLbIn (
    override var mass: Double,
    override var height: Double
) : Bmi {

    override fun isCorrectMassVal(): Boolean {
        if (mass >= Bmi.convKgToLb(Bmi.kgMassMin) && mass <= Bmi.convKgToLb(Bmi.kgMassMax))
            return true
        return false
    }

    override fun isCorrectHeightVal(): Boolean {
        if (height >= Bmi.convCmToIn(Bmi.cmHeightMin) && height <= Bmi.convCmToIn(Bmi.cmHeightMax))
            return true
        return false
    }

    override fun count(): Double {
        return (mass / (height * height)) * 703
    }
}