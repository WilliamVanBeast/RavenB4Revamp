package net.minusmc.ravenb4.module.modules.minigames

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory

class SumoFences: Module("SumoFences", ModuleCategory.minigames) {
	private val description = DescriptionSetting("SumoFences", "Fences for Hypixel sumo.")
	private val blockType = ComboSetting("BlockType", arrayOf("Oak fence", "Leaves", "Glass", "Barrier"), "Oak fence")

	private var blockState = Blocks.oak_fence.defaultState

	init {
		addSetting(description)
		addSetting(blockType)
	}

	@SubscribeEvent
	fun onMouseClick(event: MouseEvent) {
		if (event.buttonstate && (event.button == 0 || event.button == 1))
	}

	private fun guiUpdate() {
		blockState = when (blockType.get().lowercase()) {
			"barrier" -> Blocks.oak_fence.defaultState
			"leaves" -> Blocks.leaves.defaultState
			"glass" -> Blocks.glass.defaultState
			else -> Blocks.oak_fence.defaultState
		}
	}
}