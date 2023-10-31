package net.minusmc.ravenb4.utils

 import net.minecraft.entity.Entity
 import net.minecraft.init.Items
 import net.minecraft.item.ItemAxe
 import net.minecraft.item.ItemBlock
 import net.minecraft.item.ItemFishingRod
 import net.minecraft.item.ItemSword
 import net.minecraft.network.play.client.C03PacketPlayer
 import net.minecraft.potion.Potion
 import net.minecraft.util.BlockPos
 import net.minecraft.util.ChatComponentText
 import net.minusmc.ravenb4.RavenB4
 import net.minusmc.ravenb4.module.modules.others.Settings
 import net.minusmc.ravenb4.utils.RotationUtils.getRotationToEntity
 import org.lwjgl.input.Mouse
 import kotlin.math.atan2
 import kotlin.math.floor
 import kotlin.math.sqrt
 import kotlin.math.round


object PlayerUtils: MinecraftInstance() {
 	fun hotkeyToSlot(slot: Int) {
 		if (isPlayerInGame) mc.thePlayer.inventory.currentItem = slot
 	}

 	fun sendMessageToSelf(txt: String) {
 		if (isPlayerInGame) {
 			val m = ClientUtils.reformat("&7[&dR&7]&r $txt")
 			mc.thePlayer.addChatMessage(ChatComponentText(m))
 		}
 	}

 	val isPlayerInGame: Boolean
 		get() = mc.thePlayer != null && mc.theWorld != null

 	fun aim(entity: Entity, pitchOffset: Float, isSilent: Boolean) {
 		val targetRotation = getRotationToEntity(entity) ?: return
 		val pitch = targetRotation.pitch + 4f + pitchOffset
 		if (isSilent) mc.netHandler.addToSendQueue(C03PacketPlayer.C05PacketPlayerLook(targetRotation.yaw, pitch, mc.thePlayer.onGround))
 		else {
 			mc.thePlayer.rotationYaw = targetRotation.yaw
 			mc.thePlayer.rotationPitch = pitch
 		}
 	}

 	fun fovFromEntity(entity: Entity) = ((mc.thePlayer.rotationYaw - fovToEntity(entity)).toDouble() % 360.0 + 540.0) % 360.0 - 180.0

 	fun fovToEntity(entity: Entity): Float {
 		val x = entity.posX - mc.thePlayer.posX
 		val z = entity.posZ - mc.thePlayer.posZ
 		val yaw = atan2(x, z) * 57.2957795
 		return -yaw.toFloat()
 	}

 	fun fov(entity: Entity, fov: Float): Boolean {
 		var fov = (fov * 0.5f).toDouble()
 		val v = fovFromEntity(entity)
 		return (v > 0.0 && v < fov) || (-fov < v && v < 0.0)
 	}

 	fun getPlayerBPS(entity: Entity, precision: Int): Double {
 		val x = entity.posX - entity.prevPosX
 		val z = entity.posZ - entity.prevPosZ
 		val sp = sqrt(x * x + z * z) * 20.0
 		return round(sp)
 	}

 	fun playerOverAir(): Boolean {
 		val x = mc.thePlayer.posX
 		val y = mc.thePlayer.posY - 1.0
 		val z = mc.thePlayer.posZ
 		return mc.theWorld.isAirBlock(BlockPos(floor(x), floor(y), floor(z)))
 	}

 	fun playerUnderBlock(): Boolean {
 		val x = mc.thePlayer.posX
 		val y = mc.thePlayer.posY + 2.0
 		val z = mc.thePlayer.posZ
 		val blockPos = BlockPos(floor(x), floor(y), floor(z))
 		return mc.theWorld.isBlockFullCube(blockPos) || mc.theWorld.isBlockNormalCube(blockPos, false)
 	}

 	val currentPlayerSlot: Int
 		get() = mc.thePlayer.inventory.currentItem

 	fun isPlayerHoldingWeapon(): Boolean {
 		if (mc.thePlayer.currentEquippedItem == null)
 			return false
 		val item = mc.thePlayer.currentEquippedItem.item
 		return item is ItemSword || item is ItemAxe
 	}

 	fun getMaxDamageSlot(): Int {
 		var idx = -1
 		var damage = -1.0

 		for (slot in 0..8) {
 			val itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot) ?: continue
 			for (attr in itemInSlot.attributeModifiers.values()) {
 				if (attr.amount > damage) {
 					damage = attr.amount
 					idx = slot
 				}
 			}
 		}

 		return idx
 	}

 	fun getSlotDamage(slot: Int): Double {
 		val itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot) ?: return -1.0
 		for (attr in itemInSlot.attributeModifiers.values()) {
 			return attr.amount
 		}
 		return -1.0
 	}

 	fun playerWearingArmor(): ArrayList<Int> {
 		val wearingArmor = arrayListOf<Int>()
 		for (armorPiece in 0..3) {
 			if (mc.thePlayer.getCurrentArmor(armorPiece) != null)
 				wearingArmor.add(3 - armorPiece) // reverse order
 		}
 		return wearingArmor
 	}

 	fun getBlockAmountInCurrentStack(currentItem: Int): Int {
 		val itemStack = mc.thePlayer.inventory.getStackInSlot(currentItem) ?: return 0
        return if (itemStack.item is ItemBlock) itemStack.stackSize
        else 0
 	}

 	fun tryingToCombo() = Mouse.isButtonDown(0) && Mouse.isButtonDown(1)

 	fun swing() {
        val armSwingEnd =
            if (mc.thePlayer.isPotionActive(Potion.digSpeed)) 6 - (1 + mc.thePlayer.getActivePotionEffect(Potion.digSpeed).amplifier)
            else if (mc.thePlayer.isPotionActive(Potion.digSlowdown)) 6 + (1 + mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).amplifier) * 2
            else 6
        if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= armSwingEnd / 2 || mc.thePlayer.swingProgressInt < 0) {
            mc.thePlayer.swingProgressInt = -1
            mc.thePlayer.isSwingInProgress = true
        }
 	}

	fun isWeapon(): Boolean {
		if (mc.thePlayer.heldItem == null) return false
		val item = mc.thePlayer.heldItem.item
		val settingModule = RavenB4.moduleManager[Settings::class.java]!!
		return if (item is ItemSword) true
		else if (settingModule.axeWeapon.get() && item is ItemAxe) true
		else if (settingModule.rodWeapon.get() && item is ItemFishingRod) true
		else settingModule.stickWeapon.get() && item == Items.stick
	}
}