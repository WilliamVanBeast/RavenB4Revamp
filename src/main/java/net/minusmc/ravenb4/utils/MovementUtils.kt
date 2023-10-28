package net.minusmc.ravenb4.utils



object MovementUtils: MinecraftInstance() {
	fun isMoving() = mc.thePlayer.moveForward != 0f && mc.thePlayer.moveStrafing != 0f

	fun fixMovementSpeed(mul: Double, notMoving: Boolean) {
		if (!notMoving || isMoving()) {
			val yaw = correctRotations()
			mc.thePlayer.motionX = -sin(yaw) * mul
			mc.thePlayer.motionZ = cos(yaw) * mul
		}
	}

	// fun bop(mul: Double) {
	// 	var forward = mc.thePlayer.movementInput.moveForward
	// 	var strafe = mc.thePlayer.movementInput.moveStrafe
	// 	var yaw = mc.thePlayer.rotationYaw
	// 	if (forward == 0.0 && strafe == 0.0) {
	// 		mc.thePlayer.motionX = 0.0
	// 		mc.thePlayer.motionZ = 0.0
	// 	} else {
	// 		if (forward != 0.0) {
	// 			if (strafe > 0.0) yaw += (float)(forward > 0.0 ? -45 : 45)
	// 			} else if (strafe < 0.0D) {
	// 			  yaw += (float)(forward > 0.0D ? 45 : -45)
	// 			}

	// 			strafe = 0.0D
	// 			if (forward > 0.0D) {
	// 			  forward = 1.0D
	// 			} else if (forward < 0.0D) {
	// 			  forward = -1.0D
	// 		}
	// 	}

	// 	double rad = Math.toRadians(yaw + 90.0F)
	// 	double sin = Math.sin(rad)
	// 	double cos = Math.cos(rad)
	// 	mc.thePlayer.motionX = forward * s * cos + strafe * s * sin
	// 	mc.thePlayer.motionZ = forward * s * sin - strafe * s * cos
	// 	}
	// }
}
