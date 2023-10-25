package net.minusmc.ravenb4.utils

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.MathHelper
import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.modules.others.Settings
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.math.round

// bd.class
object RotationUtils: MinecraftInstance() {

	// fun a
	fun rotateToEntity(entity: Entity?) {
		entity ?: return
		val rot = getRotationToEntity(entity)
		setRotation(rot)
	}

	// fun c
	fun prevRotateToEnitty(entity: Entity?) {
		entity ?: return
		val rot = getPrevRotation(entity, mc.thePlayer.prevRotationYaw, mc.thePlayer.prevRotationPitch)
		setRotation(rot)
	}

	fun aim(entity: Entity, pitchOffset: Float, isSilent: Boolean) {
		val targetRotation = getRotationToEntity(entity) ?: return
		val pitch = targetRotation.pitch + 4f + pitchOffset
		if (isSilent) mc.netHandler.addToSendQueue(C03PacketPlayer.C05PacketPlayerLook(targetRotation.yaw, pitch, mc.thePlayer.onGround));
		else {
			mc.thePlayer.rotationYaw = targetRotation.yaw;
			mc.thePlayer.rotationPitch = pitch;
		}
	}

	// fun b
	fun getRotationToEntity(entity: Entity?): Rotation? {
		entity ?: return null

		val x = entity.posX - mc.thePlayer.posX
		val z = entity.posZ - mc.thePlayer.posZ

		val diff = if (entity is EntityLivingBase) {
			entity.posY + entity.getEyeHeight().toDouble() * 0.9 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight().toDouble()
		} else {
			(entity.entityBoundingBox.minY + entity.entityBoundingBox.maxY) / 2.0 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight().toDouble()
		}

		val distance = sqrt(x * x + z * z)
		val yaw = (atan2(z, x) * 57.295780181884766).toFloat() - 90f
		val pitch = (-atan2(diff, distance) * 57.295780181884766).toFloat()

		return Rotation(
			mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
			clamp_float(mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch))
		)
	}

	// fun g
	fun getRotationToEntity(entity: Entity?, prevYaw: Float, prewPitch: Float): Rotation? {
		val rot = getRotationToEntity(entity) ?: return null
		return getRotation(rot.yaw, rot.pitch, prevYaw, prevPitch)
	}

	// fun d
	fun getRotation(yaw: Float, pitch: Float, prevYaw: Float, prevPitch: Float): Rotation? {
		var yaw = yaw
		var pitch = pitch
		var diffYaw = prevYaw - yaw
		var diffAbsYaw = abs(diffYaw)

		val settingModule = RavenB4.moduleManager[Settings::class.java]!!

		if (settingModule.limitYawAcceleration.get()) {
			if (diffAbsYaw >= 20f)
				diffYaw += if (diffYaw > 0f) (-10 + RandomUtils.nextInt(3, 0)).toFloat() else (10 + RandomUtils.nextInt(0, 3)).toFloat()
		}

		if (settingModule.patchGCD.get()) {
			var diffPitch = pitch - prevPitch
			val diffMouseSens = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f
			var mouseValue = (diffMouseSens * diffMouseSens * diffMouseSens).toDouble() * 1.2
			val yawMouseValue = round((diffYaw.toDouble() / mouseValue) * mouseValue)
			val pitchMouseValue = round((diffPitch.toDouble() / mouseValue) * mouseValue)

			yaw = prevYaw + yawMouseValue.toFloat()
			pitch = prevPitch + pitchMouseValue.toFloat()
		}

		if (diffAbsYaw >= 1f) {
			val yawFactor = settingModule.randomYawValue.get().toInt()
			if (yawFactor != 0) {
				yawFactor = yawFactor * 100 + RandomUtils.nextInt(-30, 30)
				yaw = (yaw.toDouble() - RandomUtils.nextInt(-yawFactor, yawFactor).toDouble() / 100.0).toFloat()
			}
		} else if (diffAbsYaw.toDouble <= 0.04) {
			yaw = (yaw.toDouble() + (if (diffAbsYaw > 0f) 0.01 else -0.01)).toFloat()
		}

		return Rotation(yaw, MathHelper.clamp_float(pitch, -90f, 90f))
	}

	// fun k
	fun getRotationToBlock(blockPos: BlockPos?): Rotation? {
		blockPos ?: return
		val x = blockPos.x + 0.45 - mc.thePlayer.posX
		val y = blockPos.y + 0.45 - mc.thePlayer.posY - mc.thePlayer.getEyeHeight()
		val z = blockPos.z + 0.45 - mc.thePlayer.posZ

		val distance = sqrt(x * x + z * z)
		val yaw = (atan2(z, x) * 57.295780181884766).toFloat() - 90f
		val pitch = (-atan2(y, distance) * 57.295780181884766).toFloat()

		return Rotation(
			mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
			clamp_float(mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch))
		)
	}

	// fun e
	fun getRotationToBlock(blockPos: BlockPos?, yaw: Float, pitch: Float): Rotation? {
		val rot = getRotationToBlock(blockPos) ?: return null
		return getRotation(rot.yaw, rot.pitch, yaw, pitch)
	}

	// fun l
	fun setRotation(rotation: Rotation?) {
		rotation ?: return
		mc.thePlayer.rotationYaw = rotation.yaw
		mc.thePlayer.rotationPitch = rotation.pitch
	}

	// fun m
	fun clamp_float(value: Float, min: Float, max: Float) = MathHelper.clamp_float(value, min, max)

	// fun f
	fun nextFactor() = RandomUtils.nextInt(5, 25).toDouble() / 100.0

	// fun h -> diff?
	// fun diff(entity: Entity?, check: Boolean) {
	//	return abs()
	//}

}