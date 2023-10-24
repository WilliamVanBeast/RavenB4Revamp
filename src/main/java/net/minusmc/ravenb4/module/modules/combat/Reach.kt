package net.minusmc.ravenb4.module.modules.combat

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
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

    private fun performReachAction() {
        val minDistance: Double = minReach.get()
        val maxDistance: Double = maxReach.get()
        val entity = mc.thePlayer.rayTrace(RandomUtils.nextDouble(minDistance, maxDistance), 1.0f).entityHit;
        mc.playerController.attackEntity(mc.thePlayer, entity);
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