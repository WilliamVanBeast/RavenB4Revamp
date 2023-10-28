package net.minusmc.ravenb4.utils

object ClientUtils: MinecraftInstance() {
	fun reformat(txt: String) = txt.replace("&", "§")
}