package net.minusmc.ravenb4.module.modules.world;

import net.minecraft.block.BlockAir
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting


class Scaffold2: Module("Scaffold2", ModuleCategory.world){

    private var lastPlacementTime: Long = 0
    private val isSprinting = false

    var bestWhileMovingForward: DescriptionSetting = DescriptionSetting("Desc", "Best while moving forward.")
    var allowSprinting: TickSetting = TickSetting("Allow sprinting", false);
    var highlightBlock: TickSetting = TickSetting("Highlight block", false);
    var placeOnPost: TickSetting = TickSetting("Place on post", false)
    var safeWalk: TickSetting = TickSetting("Safewalk", true)
    var silentSwing: TickSetting = TickSetting("Silent swing", false)
    var motion: SliderSetting = SliderSetting("Motion", 1.0, 0.2, 1.0, 0.01)
    var placeDelay: SliderSetting = SliderSetting("Place delay", 50.0, 50.0, 250.0, 10.0)
    var tower: TickSetting = TickSetting("Tower", false)
    var towerSpeed: SliderSetting = SliderSetting("Tower speed", 0.42, 0.36, 0.8, 0.01)

    init {
        registerSetting(bestWhileMovingForward);
        registerSetting(allowSprinting);
        registerSetting(highlightBlock);
        registerSetting(placeOnPost);
        registerSetting(safeWalk);
        registerSetting(silentSwing);
        registerSetting(motion);
        registerSetting(placeDelay);
        registerSetting(tower);
        registerSetting(towerSpeed);
    }

    private fun handleTower() {
       if(!tower.get()) return;

        if(mc.gameSettings.keyBindJump.isKeyDown) {
            mc.thePlayer.motionY *= 0;
            mc.thePlayer.motionY = towerSpeed.get();
        }
    }

    private fun canPlaceBlock(blockHit: MovingObjectPosition): Boolean {
        val itemStack: ItemStack = mc.thePlayer.heldItem
        if (itemStack != null && itemStack.item is ItemBlock) {
            val pos = blockHit.blockPos
            lastPlacementTime = System.currentTimeMillis()
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, pos, blockHit.sideHit, blockHit.hitVec)) {
                if (silentSwing.get()) {
                    mc.thePlayer.swingItem()
                } else {
                    mc.thePlayer.swingItem()
                }
                return true
            }
        }
        return false
    }

    @SubscribeEvent
    fun onMouseEvent(event: MouseEvent) {
        if (event.buttonstate && isSprinting) {
            event.setCanceled(true)
        }
    }

    private fun findBlocksToRender(): HashMap<BlockPos, EnumFacing> {
        val blocksToRender = HashMap<BlockPos, EnumFacing>()
        val yOffset = if (mc.thePlayer.onGround) -1 else -2
        val searchRadius = 2
        for (y in yOffset..-1) {
            for (x in -searchRadius..searchRadius) {
                for (z in -searchRadius..searchRadius) {
                    val pos = BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z)
                    if (mc.theWorld.getBlockState(pos).getBlock() !is BlockAir) {
                        var facing: EnumFacing? = null
                        var distance = 0.0
                        for (side in EnumFacing.VALUES) {
                            if (side != EnumFacing.DOWN && (side != EnumFacing.UP || !mc.thePlayer.onGround && mc.thePlayer.motionY >= 0.0)) {
                                val offsetPos: BlockPos = pos.offset(side)
                                if (mc.theWorld.getBlockState(offsetPos).getBlock() is BlockAir) {
                                    val distanceToBlock = offsetPos.distanceSq(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
                                    if (facing == null || distanceToBlock < distance) {
                                        facing = side
                                        distance = distanceToBlock
                                    }
                                }
                            }
                        }
                        if (facing != null) {
                            blocksToRender[pos] = facing
                        }
                    }
                }
            }
        }
        return blocksToRender
    }


}