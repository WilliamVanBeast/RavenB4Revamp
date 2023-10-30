package net.minusmc.ravenb4.utils

import kotlin.math.round
import kotlin.math.pow

object MathUtils {
	fun round(num: Double, precision: Int): Double {
		if (precision == 0) return round(num)
		val p2 = 10.0.pow(precision)
		return round(num * p2) / p2
	}
}