package net.minusmc.ravenb4.utils

// import kotlin.math.atan2
// import kotlin.math.sqrt
// import kotlin.math.round


// object PlayerUtils: MinecraftInstance() {
// 	fun hotkeyToSlot(slot: Int) {
// 		if (isPlayerInGame) mc.thePlayer.inventory.currentItem = slot
// 	}

// 	fun sendMessageToSelf(txt: String) {
// 		if (isPlayerInGame) {
// 			val m = ClientUtils.reformat("&7[&dR&7]&r $txt")
// 			mc.thePlayer.addChatMessage(ChatComponentText(m))
// 		}
// 	}

// 	val isPlayerInGame: Boolean
// 		get() = mc.thePlayer != null && mc.theWorld != null

// 	fun aim(entity: Entity, pitchOffset: Float, isSilent: Boolean) {
// 		val targetRotation = getRotationToEntity(entity) ?: return
// 		val pitch = targetRotation.pitch + 4f + pitchOffset
// 		if (isSilent) mc.netHandler.addToSendQueue(C03PacketPlayer.C05PacketPlayerLook(targetRotation.yaw, pitch, mc.thePlayer.onGround))
// 		else {
// 			mc.thePlayer.rotationYaw = targetRotation.yaw
// 			mc.thePlayer.rotationPitch = pitch
// 		}
// 	}

// 	fun fovFromEntity(entity: Entity) = ((mc.thePlayer.rotationYaw - fovToEntity(entity)).toDouble() % 360.0 + 540.0) % 360.0 - 180.0

// 	fun fovToEntity(entity: Entity): Float {
// 		val x = entity.posX - mc.thePlayer.posX
// 		val z = entity.posZ - mc.thePlayer.posZ
// 		val yaw = atan2(x, z) * 57.2957795
// 		return -yaw.toFloat()
// 	}

// 	fun fov(entity: Entity, fov: Float): Boolean {
// 		var fov = (fov * 0.5f).toDouble()
// 		val v = fovFromEntity(entity)
// 		return (v > 0.0 && v < fov) || (-fov < v && v < 0.0)
// 	}

// 	fun getPlayerBPS(entity: Entity, precision: Int): Double {
// 		val x = en.posX - en.prevPosX
// 		val z = en.posZ - en.prevPosZ
// 		val sp = sqrt(x * x + z * z) * 20.0
// 		return round(sp, precision)
// 	}

// 	fun playerOverAir(): Boolean {
// 		val x = mc.thePlayer.posX
// 		val y = mc.thePlayer.posY - 1.0
// 		val z = mc.thePlayer.posZ
// 		return mc.theWorld.isAirBlock(BlockPos(floor(x), floor(y), floor(z)))
// 	}

// 	fun playerUnderBlock(): Boolean {
// 		val x = mc.thePlayer.posX
// 		val y = mc.thePlayer.posY + 2.0
// 		val z = mc.thePlayer.posZ
// 		val blockPos = BlockPos(floor(x), floor(y), floor(z))
// 		return mc.theWorld.isBlockFullCube(p) || mc.theWorld.isBlockNormalCube(p, false)
// 	}

// 	val currentPlayerSlot: Int
// 		get() = mc.thePlayer.inventory.currentItem
	
// 	fun isPlayerHoldingWeapon(): Boolean {
// 		if (mc.thePlayer.getCurrentEquippedItem() == null)
// 			return false
// 		val item = mc.thePlayer.getCurrentEquippedItem().item
// 		return item is ItemSword || item is ItemAxe
// 	}

// 	fun getMaxDamageSlot(): Int {
// 		var idx = -1
// 		var damage = -1

// 		for (slot in 0..8) {
// 			val itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot) ?: continue
// 			for (attr in itemInSlot.getAttributeModifiers().values()) {
// 				if (attr.amount > damage) {
// 					damage = attr.amount
// 					index = slot
// 				}
// 			}
// 		}

// 		return idx
// 	}

// 	fun getSlotDamage(slot: Int) {
// 		val itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot) ?: return -1
// 		for (attr in itemInSlot.getAttributeModifiers().values()) {
// 			return attr.amount
// 		}
// 		return -1
// 	}

// 	fun playerWearingArmor(): ArrayList<Int> {
// 		val wearingArmor = arrayListOf()
// 		for (armorPiece in 0..3) {
// 			if (mc.thePlayer.getCurrentArmor(armorPiece) != null) 
// 				wearingArmor.add(3 - armorPiece) // reverse order
// 		}
// 		return wearingArmor
// 	}

// 	fun getBlockAmountInCurrentStack(currentItem: Int): Int {
// 		val itemStack = mc.thePlayer.inventory.getStackInSlot(currentItem) ?: return 0
// 		if (itemStack is ItemBlock) return itemStack.stackSize
// 		else return 0
// 	}

// 	fun tryingToCombo() = Mouse.isButtonDown(0) && Mouse.isButtonDown(1)

// 	fun float correctRotations() {
// 	 float yw = mc.thePlayer.rotationYaw
// 	 if (mc.thePlayer.moveForward < 0.0F) {
// 	    yw += 180.0F
// 	 }

// 	 float f
// 	 if (mc.thePlayer.moveForward < 0.0F) {
// 	    f = -0.5F
// 	 } else if (mc.thePlayer.moveForward > 0.0F) {
// 	    f = 0.5F
// 	 } else {
// 	    f = 1.0F
// 	 }

// 	 if (mc.thePlayer.moveStrafing > 0.0F) {
// 	    yw -= 90.0F * f
// 	 }
// 	 if (mc.thePlayer.moveStrafing < 0.0F) {
// 	    yw += 90.0F * f
// 	 }

// 	 yw *= 0.017453292F
// 	 return yw
// 	}

// 	fun double pythagorasMovement() {
// 	 return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)
// 	}

// 	fun void swing() {
// 	 EntityPlayerSP p = mc.thePlayer
// 	 int armSwingEnd = p.isPotionActive(Potion.digSpeed) ? 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (p.isPotionActive(Potion.digSlowdown) ? 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6)
// 	 if (!p.isSwingInProgress || p.swingProgressInt >= armSwingEnd / 2 || p.swingProgressInt < 0) {
// 	    p.swingProgressInt = -1
// 	    p.isSwingInProgress = true
// 	 }

// 	}
}