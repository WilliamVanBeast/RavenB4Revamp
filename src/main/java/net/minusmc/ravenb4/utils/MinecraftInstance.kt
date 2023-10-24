package net.minusmc.ravenb4.utils

import net.minecraft.client.Minecraft

object MinecraftInstance {
	@JvmStatic
	val mc = Minecraft.getMinecraft()
}