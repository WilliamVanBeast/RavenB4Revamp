package net.minusmc.ravenb4.utils

import net.minusmc.ravenb4.utils.RotationUtils.correctRotations
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object MovementUtils : MinecraftInstance() {
    fun isMoving() = mc.thePlayer.moveForward != 0f && mc.thePlayer.moveStrafing != 0f

    fun fixMovementSpeed(mul: Double, notMoving: Boolean) {
        if (!notMoving || isMoving()) {
            val yaw = correctRotations()
            mc.thePlayer.motionX = -sin(yaw) * mul
            mc.thePlayer.motionZ = cos(yaw) * mul
        }
    }

    fun distance(): Double =
        sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)

    fun bop(mul: Double) {
        var forward = mc.thePlayer.movementInput.moveForward
        var strafe = mc.thePlayer.movementInput.moveStrafe
        var yaw = mc.thePlayer.rotationYaw
        if (forward == 0f && strafe == 0f) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        } else {
            if (forward != 0f) {
                if (strafe > 0f) yaw += if (forward > 0f) -45f else 45f
            } else if (strafe < 0.0) {
                yaw += if (forward > 0f) -45f else 45f
            }

            strafe = 0f
            if (forward > 0.0) forward = 1f
            else if (forward < 0.0) forward = -1f
        }

        val rad = Math.toRadians(yaw.toDouble() + 90.0)
        val sin = sin(rad)
        val cos = cos(rad)
        mc.thePlayer.motionX = forward * mul * cos + strafe * mul * sin
        mc.thePlayer.motionZ = forward * mul * sin - strafe * mul * cos
    }
}
