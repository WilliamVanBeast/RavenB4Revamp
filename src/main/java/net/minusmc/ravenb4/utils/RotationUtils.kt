package net.minusmc.ravenb4.utils

import net.minecraft.util.MathHelper
import kotlin.math.sqrt
import kotlin.math.atan2

object RotationUtils: MinecraftInstance {

	fun aim(entity: Entity, ps: Float, pc: Boolean) {
		entity ?: return

		val t = getRotationToEntity(entity)
		t ?: return

		val pitch = t.pitch + 4f + ps
		if (pc) mc.netHandler.addToSendQueue(new C05PacketPlayerLook(y, p, mc.thePlayer.onGround));

         if (en != null) {
            float[] t = getTargetRotations(en);
            if (t != null) {
               float y = t[0];
               float p = t[1] + 4.0F + ps;
               if (pc) {
                  
               } else {
                  mc.thePlayer.rotationYaw = y;
                  mc.thePlayer.rotationPitch = p;
               }
            }

         }
      }

	fun getRotationToEntity(entity: Entity): Rotation? {
		entity ?: return null

		val x = entity.posX - mc.thePlayer.posX
		val z = entity.posZ - mc.thePlayer.posZ
		var diff = 0.0

		if (entity is EntityLivingBase) {
			diff = entity.posY + entity.getEyeHeight().toDouble() * 0.9 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight.toDouble()
		} else {
			diff = (entity.entityBoundingBox.minY + entity.entityBoundingBox.maxY) / 2.0 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight().toDouble()
		}

		val distance = sqrt(x * x + z * z)
		val yaw = (atan2(x, z) * 57.295780181884766).toFloat() - 90f
		val pitch = (-atan2(diff, distance) * 57.295780181884766).toFloat()

		return Rotation(
			mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
			mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)
		)
	}

	fun setRotation(rotation: Rotation) {
		rotation ?: return
		mc.thePlayer.rotationYaw = rotation.yaw
		mc.thePlayer.rotationPitch = rotation.pitch
	}

}