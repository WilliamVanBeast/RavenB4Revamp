package net.minusmc.ravenb4.module.modules.combat

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import org.apache.commons.lang3.RandomUtils


class Reach: Module("Reach", ModuleCategory.combat){
    private val minReach: SliderSetting = SliderSetting("Min Reach", 3.1, 3.0, 6.0, 0.05)
    private val maxReach: SliderSetting = SliderSetting("Max Reach", 3.3, 3.0, 6.0, 0.05)
    private val weaponOnly: TickSetting = TickSetting("Weapon Only", false)
    private val movingOnly: TickSetting = TickSetting("Moving Only", false)
    private val sprintOnly: TickSetting = TickSetting("Sprint Only", false)
    private val hitThroughBlocks: TickSetting = TickSetting("Hit Through Blocks", false)

    init {
        addSetting(minReach)
        addSetting(maxReach)
        addSetting(weaponOnly)
        addSetting(movingOnly)
        addSetting(sprintOnly)
        addSetting(hitThroughBlocks)
    }

    @SubscribeEvent
    fun onMouseClick(event: MouseEvent) {
        if (event.button < 0 || !event.buttonstate) {
            return
        }
        if (isConditionsMet()) {
            performReachAction()
        }
    }

    private fun rayTrace(zzD: Double, zzE: Double): Array<Any>? {
        var zzD = zzD
        val entity1 = mc.renderViewEntity
        var entity: Entity? = null
        return if (entity1 == null) {
            null
        } else {
            mc.mcProfiler.startSection("pick")
            val eyes_positions = entity1.getPositionEyes(1.0f)
            val look = entity1.getLook(1.0f)
            val new_eyes_pos = eyes_positions.addVector(look.xCoord * zzD, look.yCoord * zzD, look.zCoord * zzD)
            var zz6: Vec3? = null
            val zz8 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                entity1,
                entity1.entityBoundingBox.addCoord(look.xCoord * zzD, look.yCoord * zzD, look.zCoord * zzD)
                    .expand(1.0, 1.0, 1.0)
            )
            var zz9 = zzD
            for (o in zz8) {
                if (o.canBeCollidedWith()) {
                    val ex = (o.collisionBorderSize.toDouble())
                    var zz13 = o.entityBoundingBox.expand(ex, ex, ex)
                    zz13 = zz13.expand(zzE, zzE, zzE)
                    val zz14 = zz13.calculateIntercept(eyes_positions, new_eyes_pos)
                    if (zz13.isVecInside(eyes_positions)) {
                        if (0.0 < zz9 || zz9 == 0.0) {
                            entity = o
                            zz6 = if (zz14 == null) eyes_positions else zz14.hitVec
                            zz9 = 0.0
                        }
                    } else if (zz14 != null) {
                        val zz15 = eyes_positions.distanceTo(zz14.hitVec)
                        if (zz15 < zz9 || zz9 == 0.0) {
                            if (o === entity1.ridingEntity) {
                                if (zz9 == 0.0) {
                                    entity = o
                                    zz6 = zz14.hitVec
                                }
                            } else {
                                entity = o
                                zz6 = zz14.hitVec
                                zz9 = zz15
                            }
                        }
                    }
                }
            }
            if (zz9 < zzD && entity !is EntityLivingBase && entity !is EntityItemFrame) {
                entity = null
            }
            mc.mcProfiler.endSection()
            if (entity != null && zz6 != null) {
                arrayOf<Any>(entity, zz6)
            } else {
                null
            }
        }
    }

    private fun performReachAction(): Boolean {
        val minDistance: Double = minReach.get()
        val maxDistance: Double = maxReach.get()

        val distance: Double = RandomUtils.nextDouble(minDistance, maxDistance)
        val objectArray: Array<Any> = rayTrace(distance, 0.0) ?: return false
        val entity: Entity = objectArray[0] as Entity
        mc.objectMouseOver = MovingObjectPosition(entity, objectArray[1] as Vec3)
        mc.pointedEntity = entity

        mc.playerController.attackEntity(mc.thePlayer, entity);
        return true
    }

    private fun isConditionsMet(): Boolean {
        return !(weaponOnly.get() && isPlayerHoldingWeapon()) &&
                !(movingOnly.get() && isPlayerNotMoving()) &&
                !(sprintOnly.get() && !isPlayerSprinting()) &&
                !(hitThroughBlocks.get() && isHittingBlock())
    }

    private fun isPlayerHoldingWeapon(): Boolean {
        return mc.thePlayer.getHeldItem() != null
    }

    private fun isPlayerNotMoving(): Boolean {
        return mc.thePlayer.motionX === 0.0 && mc.thePlayer.motionZ === 0.0
    }

    private fun isPlayerSprinting(): Boolean {
        return mc.thePlayer.isSprinting()
    }

    private fun isHittingBlock(): Boolean {
        val blockPos: BlockPos = mc.thePlayer.rayTrace(200.0, 1.0f).getBlockPos()
        return blockPos != null && mc.theWorld.getBlockState(blockPos).getBlock() !== Blocks.air
    }

}