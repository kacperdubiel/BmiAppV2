package com.firentis.bmiapp

import com.firentis.bmiapp.bmi.Bmi
import com.firentis.bmiapp.bmi.BmiForKgCm
import com.firentis.bmiapp.bmi.BmiForLbIn
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

class BmiTests : FunSpec({
    context("Conversions") {
        test("conversion should be equal to") {
            Bmi.convKgToLb(100.0) shouldBe (220.46 plusOrMinus 0.01)
            Bmi.convLbToKg(220.46) shouldBe (100.0 plusOrMinus 0.01)

            Bmi.convCmToIn(100.0) shouldBe (39.37 plusOrMinus 0.01)
            Bmi.convInToCm(39.37) shouldBe (100.0 plusOrMinus 0.01)
        }
    }

    context("Values in [kg] and [cm]") {
        val bmiKgCm = BmiForKgCm(0.0, 0.0)

        test("mass [kg] should be correct") {
            bmiKgCm.mass = 50.0
            bmiKgCm.isCorrectMassVal() shouldBe true
            bmiKgCm.mass = 123.45
            bmiKgCm.isCorrectMassVal() shouldBe true
            bmiKgCm.mass = 222.222222
            bmiKgCm.isCorrectMassVal() shouldBe true
        }

        test("mass [kg] should not be correct") {
            bmiKgCm.mass = 0.0
            bmiKgCm.isCorrectMassVal() shouldBe false
            bmiKgCm.mass = -20.56
            bmiKgCm.isCorrectMassVal() shouldBe false
            bmiKgCm.mass = 999.99
            bmiKgCm.isCorrectMassVal() shouldBe false
        }

        test("height [cm] should be correct") {
            bmiKgCm.height = 100.0
            bmiKgCm.isCorrectHeightVal() shouldBe true
            bmiKgCm.height = 123.45
            bmiKgCm.isCorrectHeightVal() shouldBe true
            bmiKgCm.height = 222.222222
            bmiKgCm.isCorrectHeightVal() shouldBe true
        }

        test("height [cm] should not be correct") {
            bmiKgCm.height = 0.0
            bmiKgCm.isCorrectHeightVal() shouldBe false
            bmiKgCm.height = -20.56
            bmiKgCm.isCorrectHeightVal() shouldBe false
            bmiKgCm.height = 999.99
            bmiKgCm.isCorrectHeightVal() shouldBe false
        }
    }

    context("Values in [lb] and [in]") {
        val bmiLbIn = BmiForLbIn(0.0, 0.0)

        test("mass [lb] should be correct") {
            bmiLbIn.mass = 100.0
            bmiLbIn.isCorrectMassVal() shouldBe true
            bmiLbIn.mass = 234.56
            bmiLbIn.isCorrectMassVal() shouldBe true
            bmiLbIn.mass = 350.555555
            bmiLbIn.isCorrectMassVal() shouldBe true
        }

        test("mass [lb] should not be correct") {
            bmiLbIn.mass = 0.0
            bmiLbIn.isCorrectMassVal() shouldBe false
            bmiLbIn.mass = -20.56
            bmiLbIn.isCorrectMassVal() shouldBe false
            bmiLbIn.mass = 9999.99
            bmiLbIn.isCorrectMassVal() shouldBe false
        }

        test("height [in] should be correct") {
            bmiLbIn.height = 30.0
            bmiLbIn.isCorrectHeightVal() shouldBe true
            bmiLbIn.height = 66.12
            bmiLbIn.isCorrectHeightVal() shouldBe true
            bmiLbIn.height = 77.7777777
            bmiLbIn.isCorrectHeightVal() shouldBe true
        }

        test("height [in] should not be correct") {
            bmiLbIn.height = 0.0
            bmiLbIn.isCorrectHeightVal() shouldBe false
            bmiLbIn.height = -20.56
            bmiLbIn.isCorrectHeightVal() shouldBe false
            bmiLbIn.height = 999.99
            bmiLbIn.isCorrectHeightVal() shouldBe false
        }
    }

    context("Bmi measures") {
        context("in [kg] and [cm]") {
            val bmiKgCm = BmiForKgCm(0.0, 0.0)

            test("bmi measure should be equal to") {
                bmiKgCm.mass = 100.0
                bmiKgCm.height = 100.0
                bmiKgCm.count() shouldBe (100.0 plusOrMinus 0.01)

                bmiKgCm.mass = 56.78
                bmiKgCm.height = 167.89
                bmiKgCm.count() shouldBe (20.14 plusOrMinus 0.01)
            }
        }

        context("in [lb] and [in]") {
            val bmiLbIn = BmiForLbIn(0.0, 0.0)

            test("bmi measure should be equal to") {
                bmiLbIn.mass = 50.0
                bmiLbIn.height = 50.0
                bmiLbIn.count() shouldBe (14.06 plusOrMinus 0.01)

                bmiLbIn.mass = 56.789
                bmiLbIn.height = 45.6789
                bmiLbIn.count() shouldBe (19.13 plusOrMinus 0.01)
            }
        }
    }

})
