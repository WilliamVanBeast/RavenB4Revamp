package net.minusmc.ravenb4.utils

import kotlin.random.Random
import net.minusmc.ravenb4.setting.impl.DoubleSliderSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting

object RandomUtils {
	fun nextString(length: Int): String {
		val chars = ('a'..'z') + ('0'..'9')
		return (1..length).map{chars.random()}.joinToString("")
	}

	fun nextInt(minimum: Int, maximum: Int) = Random.nextInt(maximum - minimum + 1) + minimum

	fun nextFactor() = RandomUtils.nextInt(5, 25).toDouble() / 100.0

	fun nextDouble(start: Double, end: Double) = if (start == end) start else start + Random.nextDouble() * (end - start)

	fun nextDouble(value1: SliderSetting, value2: SliderSetting) = nextDouble(value1.get(), value2.get())

	fun nextDouble(value: DoubleSliderSetting) = nextDouble(value.min, value.max)
}