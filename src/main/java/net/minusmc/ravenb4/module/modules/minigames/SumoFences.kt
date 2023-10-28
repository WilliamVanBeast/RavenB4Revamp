package net.minusmc.ravenb4.module.modules.minigames

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.ComboSetting
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.utils.ClientUtils
import net.minusmc.ravenb4.utils.PlayerUtils
import org.lwjgl.input.Mouse
import java.util.Timer
import java.util.TimerTask


class SumoFences: Module("SumoFences", ModuleCategory.minigames) {
	private val description = DescriptionSetting("SumoFences", "Fences for Hypixel sumo.")
	private val fenceHeight = SliderSetting("Fence height", 4.0, 1.0, 6.0, 1.0)
	private val blockType = ComboSetting("BlockType", arrayOf("Oak fence", "Leaves", "Glass", "Barrier"), "Oak fence")

	private val blockPoses = arrayOf(
		BlockPos(9, 65, -2),
		BlockPos(9, 65, -1),
		BlockPos(9, 65, 0),
		BlockPos(9, 65, 1),
		BlockPos(9, 65, 2),
		BlockPos(9, 65, 3),
		BlockPos(8, 65, 3),
		BlockPos(8, 65, 4),
		BlockPos(8, 65, 5),
		BlockPos(7, 65, 5),
		BlockPos(7, 65, 6),
		BlockPos(7, 65, 7),
		BlockPos(6, 65, 7),
		BlockPos(5, 65, 7),
		BlockPos(5, 65, 8),
		BlockPos(4, 65, 8),
		BlockPos(3, 65, 8),
		BlockPos(3, 65, 9),
		BlockPos(2, 65, 9),
		BlockPos(1, 65, 9),
		BlockPos(0, 65, 9),
		BlockPos(-1, 65, 9),
		BlockPos(-2, 65, 9),
		BlockPos(-3, 65, 9),
		BlockPos(-3, 65, 8),
		BlockPos(-4, 65, 8),
		BlockPos(-5, 65, 8),
		BlockPos(-5, 65, 7),
		BlockPos(-6, 65, 7),
		BlockPos(-7, 65, 7),
		BlockPos(-7, 65, 6),
		BlockPos(-7, 65, 5),
		BlockPos(-8, 65, 5),
		BlockPos(-8, 65, 4),
		BlockPos(-8, 65, 3),
		BlockPos(-9, 65, 3),
		BlockPos(-9, 65, 2),
		BlockPos(-9, 65, 1),
		BlockPos(-9, 65, 0),
		BlockPos(-9, 65, -1),
		BlockPos(-9, 65, -2),
		BlockPos(-9, 65, -3),
		BlockPos(-8, 65, -3),
		BlockPos(-8, 65, -4),
		BlockPos(-8, 65, -5),
		BlockPos(-7, 65, -5),
		BlockPos(-7, 65, -6),
		BlockPos(-7, 65, -7),
		BlockPos(-6, 65, -7),
		BlockPos(-5, 65, -7),
		BlockPos(-5, 65, -8),
		BlockPos(-4, 65, -8),
		BlockPos(-3, 65, -8),
		BlockPos(-3, 65, -9),
		BlockPos(-2, 65, -9),
		BlockPos(-1, 65, -9),
		BlockPos(0, 65, -9),
		BlockPos(1, 65, -9),
		BlockPos(2, 65, -9),
		BlockPos(3, 65, -9),
		BlockPos(3, 65, -8),
		BlockPos(4, 65, -8),
		BlockPos(5, 65, -8),
		BlockPos(5, 65, -7),
		BlockPos(6, 65, -7),
		BlockPos(7, 65, -7),
		BlockPos(7, 65, -6),
		BlockPos(7, 65, -5),
		BlockPos(8, 65, -5),
		BlockPos(8, 65, -4),
		BlockPos(8, 65, -3),
		BlockPos(9, 65, -3)
	)

	private var timer: Timer? = null

	init {
		addSetting(description)
		addSetting(blockType)
	}

	override fun onEnable() {
		timer = Timer()
		timer!!.scheduleAtFixedRate(object: TimerTask() {
			override fun run() {
				if (!isRightMap()) return
				for (blockPos in blockPoses) {
					for (i in 0..fenceHeight.get().toInt()) {
						val blockPos2 = BlockPos(blockPos.x, blockPos.y + i, blockPos.z)
						if (mc.theWorld.getBlockState(blockPos2).block == Blocks.air)
							mc.theWorld.setBlockState(blockPos2, blockState)
					}
				}
			}
		}, 0L, 500L)
	}


	override fun onDisable() {
		if (timer != null) {
			timer!!.cancel()
			timer!!.purge()
			timer == null
		}

		for (blockPos in blockPoses) {
			for (i in 0..fenceHeight.get().toInt()) {
				val blockPos2 = BlockPos(blockPos.x, blockPos.y + i, blockPos.z)
				if (mc.theWorld.getBlockState(blockPos2) == blockState)
					mc.theWorld.setBlockState(blockPos2, Blocks.air.defaultState)
			}
		}


	}
	@SubscribeEvent
	fun onMouseClick(event: MouseEvent) {
		if (event.buttonstate && (event.button == 0 || event.button == 1) && PlayerUtils.isPlayerInGame && isRightMap()) {
			val movingObjPos = mc.objectMouseOver ?: return
			if (movingObjPos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				val x = movingObjPos.blockPos.x
				val z = movingObjPos.blockPos.z

				for (blockPos in blockPoses) {
					if (blockPos.x == x && blockPos.z == z) {
						event.isCanceled = true
						if (event.button == 0) PlayerUtils.swing()
						Mouse.poll()
						break;
					}
				}
			}
		}
	}

	private fun isRightMap(): Boolean {
		if (!ClientUtils.isHypixel()) return false
		for (line in ClientUtils.getPlayersFromScoreboard()) {
			val s = ClientUtils.cleanString(line)
			if (s.startsWith("Map:"))
				if (blockType.get().contains(s.substring(5))) return true
			else if (s.equals("Mode: Sumo Duel", true)) return true
		}
		return false
	}

	private val blockState: IBlockState
		get() = when (blockType.get().lowercase()) {
			"barrier" -> Blocks.oak_fence.defaultState
			"leaves" -> Blocks.leaves.defaultState
			"glass" -> Blocks.glass.defaultState
			else -> Blocks.oak_fence.defaultState
		}
}