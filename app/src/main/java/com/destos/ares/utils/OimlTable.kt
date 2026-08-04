package com.destos.ares.utils

import kotlin.math.abs

/**
 * OIML R-22 tabanlı alkolmetre sıcaklık düzeltme tablosu.
 * Referans: 20°C | Bilinear interpolasyonlu
 * Doğrulama: 45.0° @ 25°C → -1.75 → gerçek derece 43.25
 */
object OimlTable {

    private val TEMPS = doubleArrayOf(
        0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0
    )
    private val DEGREES = doubleArrayOf(
        5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0,
        55.0, 60.0, 65.0, 70.0, 75.0, 80.0, 85.0, 90.0, 95.0, 100.0
    )

    private val TABLE = arrayOf(
        doubleArrayOf(2.60, 3.20, 3.80, 4.40, 5.00, 5.60, 6.20, 6.60, 7.00, 7.40, 7.80, 8.20, 8.60, 9.00, 9.40, 9.80, 10.20, 10.60, 11.00, 11.40),
        doubleArrayOf(1.95, 2.40, 2.85, 3.30, 3.75, 4.20, 4.65, 4.95, 5.25, 5.55, 5.85, 6.15, 6.45, 6.75, 7.05, 7.35, 7.65, 7.95, 8.25, 8.55),
        doubleArrayOf(1.30, 1.60, 1.90, 2.20, 2.50, 2.80, 3.10, 3.30, 3.50, 3.70, 3.90, 4.10, 4.30, 4.50, 4.70, 4.90, 5.10, 5.30, 5.50, 5.70),
        doubleArrayOf(0.65, 0.80, 0.95, 1.10, 1.25, 1.40, 1.55, 1.65, 1.75, 1.85, 1.95, 2.05, 2.15, 2.25, 2.35, 2.45, 2.55, 2.65, 2.75, 2.85),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(-0.65, -0.80, -0.95, -1.10, -1.25, -1.40, -1.55, -1.65, -1.75, -1.85, -1.95, -2.05, -2.15, -2.25, -2.35, -2.45, -2.55, -2.65, -2.75, -2.85),
        doubleArrayOf(-1.30, -1.60, -1.90, -2.20, -2.50, -2.80, -3.10, -3.30, -3.50, -3.70, -3.90, -4.10, -4.30, -4.50, -4.70, -4.90, -5.10, -5.30, -5.50, -5.70),
        doubleArrayOf(-1.95, -2.40, -2.85, -3.30, -3.75, -4.20, -4.65, -4.95, -5.25, -5.55, -5.85, -6.15, -6.45, -6.75, -7.05, -7.35, -7.65, -7.95, -8.25, -8.55),
        doubleArrayOf(-2.60, -3.20, -3.80, -4.40, -5.00, -5.60, -6.20, -6.60, -7.00, -7.40, -7.80, -8.20, -8.60, -9.00, -9.40, -9.80, -10.20, -10.60, -11.00, -11.40)
    )

    fun correction(measuredDegree: Double, measuredTemp: Double): Double {
        if (measuredDegree <= 0 || measuredDegree > 100) return 0.0
        if (abs(measuredTemp - 20.0) < 0.0001) return 0.0

        val t = measuredTemp.coerceIn(TEMPS.first(), TEMPS.last())
        val d = measuredDegree.coerceIn(DEGREES.first(), DEGREES.last())

        var tLow = 0
        while (tLow < TEMPS.size - 2 && TEMPS[tLow + 1] < t) tLow++
        val tHigh = tLow + 1

        var dLow = 0
        while (dLow < DEGREES.size - 2 && DEGREES[dLow + 1] < d) dLow++
        val dHigh = dLow + 1

        val q11 = TABLE[tLow][dLow]
        val q12 = TABLE[tLow][dHigh]
        val q21 = TABLE[tHigh][dLow]
        val q22 = TABLE[tHigh][dHigh]

        val dFrac = (d - DEGREES[dLow]) / (DEGREES[dHigh] - DEGREES[dLow])
        val tFrac = (t - TEMPS[tLow]) / (TEMPS[tHigh] - TEMPS[tLow])

        val top = q11 + dFrac * (q12 - q11)
        val bottom = q21 + dFrac * (q22 - q21)
        return top + tFrac * (bottom - top)
    }
}
