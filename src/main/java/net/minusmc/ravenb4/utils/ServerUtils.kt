package net.minusmc.ravenb4.utils

object ServerUtils: MinecraftInstance() {
	@JvmStatic
	val playerName = ""

	fun getName() = if (playerName.isEmpty()) mc.thePlayer.getName() else playerName
}