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
import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.utils.RandomUtils
import net.minusmc.ravenb4.utils.PlayerUtils
import org.lwjgl.input.Mouse


class Reach: Module("Reach", ModuleCategory.combat) {
    private val minReach = SliderSetting("Min Reach", 3.1, 3.0, 6.0, 0.05)
    private val maxReach = SliderSetting("Max Reach", 3.3, 3.0, 6.0, 0.05)
    private val weaponOnly = TickSetting("Weapon Only", false)
    private val movingOnly = TickSetting("Moving Only", false)
    private val sprintOnly = TickSetting("Sprint Only", false)
    private val hitThroughBlocks = TickSetting("Hit Through Blocks", false)

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
        if (!PlayerUtils.isPlayerInGame || event.button < 0 || !event.buttonstate) return
        val autoClicker = RavenB4.moduleManager[AutoClicker::class.java]!!
        if (autoClicker.state && autoClicker.leftClicker.get() && Mouse.isButtonDown(0)) return
        canReach()
    }

    fun rayTrace(reachMax: Double, exMul: Double): Array<Any>? {
        var reachMax = if (!state) {
            if (mc.playerController.extendedReach()) 6.0 else 3.0
        } else reachMax

        val entityRender = mc.renderViewEntity ?: return null
        var entitySelected: Entity? = null

        mc.mcProfiler.startSection("pick")
        val eyePos = entityRender.getPositionEyes(1.0f)
        val look = entityRender.getLook(1.0f)
        val vector = eyePos.addVector(look.xCoord * reachMax, look.yCoord * reachMax, look.zCoord * reachMax)
        var hitVec: Vec3? = null
        val entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(entityRender, entityRender.entityBoundingBox.addCoord(look.xCoord * reachMax, look.yCoord * reachMax, look.zCoord * reachMax).expand(1.0, 1.0, 1.0))
        
        var reach = reachMax
        for (entity in entities) {
            if (entity.canBeCollidedWith()) {
                val collisionSize = entity.collisionBorderSize.toDouble()
                var boudingBox = entity.entityBoundingBox.expand(collisionSize, collisionSize, collisionSize)
                boudingBox = boudingBox.expand(exMul, exMul, exMul)
                val movingObjectPos = boudingBox.calculateIntercept(eyePos, vector)
                if (boudingBox.isVecInside(eyePos)) {
                    if (reach >= 0.0) {
                        entitySelected = entity
                        hitVec = if (movingObjectPos == null) eyePos else movingObjectPos.hitVec
                        reach = 0.0
                    }
                } else if (movingObjectPos != null) {
                    val dist = eyePos.distanceTo(movingObjectPos.hitVec)
                    if (dist < reach || reach == 0.0) {
                        if (entity == entityRender.ridingEntity) {
                            if (reach == 0.0) {
                                entitySelected = entity
                                hitVec = movingObjectPos.hitVec
                            }
                        } else {
                            entitySelected = entity
                            hitVec = movingObjectPos.hitVec
                            reach = dist
                        }
                    }
                }
            }
        }
        if (reach < reachMax && entitySelected !is EntityLivingBase && entitySelected !is EntityItemFrame) {
            entitySelected = null
        }
        mc.mcProfiler.endSection()
        return if (entitySelected != null && hitVec != null)
            arrayOf<Any>(entitySelected, hitVec)
        else null
    }

    fun canReach(): Boolean {
        if (!PlayerUtils.isPlayerInGame) return false
        if (weaponOnly.get() && !PlayerUtils.isCurrentHeldWeapon()) return false
        if (movingOnly.get() && mc.thePlayer.motionX == 0.0 && mc.thePlayer.motionZ == 0.0) return false
        if (sprintOnly.get() && !mc.thePlayer.isSprinting) return false

        if (!hitThroughBlocks.get() && mc.objectMouseOver != null) {
            val blockPos = mc.objectMouseOver.blockPos
            if (blockPos != null && mc.theWorld.getBlockState(blockPos).block != Blocks.air)
                return false
        }

        val distance = RandomUtils.nextDouble(minReach.get(), maxReach.get())
        val objectArray = rayTrace(distance, 0.0) ?: return false
        val entity = objectArray[0] as Entity
        mc.objectMouseOver = MovingObjectPosition(entity, objectArray[1] as Vec3)
        mc.pointedEntity = entity
        return true
    }

}