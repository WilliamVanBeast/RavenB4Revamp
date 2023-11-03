package net.minusmc.ravenb4.utils

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.tweaker.MinecraftFields


object FieldUtils {
	var errorInitFields = false

	fun checkModule(module: Module): Boolean {
		if (!errorInitFields) return false
		PlayerUtils.sendMessageToSelf("&cThere was an error, relaunch the game.")
		module.unregister()
		return true
	}

	fun init() {
		for (mcField in MinecraftFields.values()) {
			if (!mcField.method.isEmpty()) {
				try {
					mcField.field = mcField.clazz.getDeclaredField(mcField.method)
				} catch (e: NoSuchFieldException) {}
			}

			if (!mcField.isInited()) {
				try {
					mcField.field = mcField.clazz.getDeclaredField(mcField.method)
				} catch (e: NoSuchFieldException) {
					errorInitFields = true
					return
				}
			}

			mcField.field.isAccessible = true
		}
	}
}