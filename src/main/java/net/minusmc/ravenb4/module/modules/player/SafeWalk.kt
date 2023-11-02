package net.minusmc.ravenb4.module.modules.player

import net.minecraft.client.Minecraft
import net.minecraft.item.ItemBlock
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.utils.PlayerUtils
import org.lwjgl.input.Keyboard


class SafeWalk: Module("SafeWalk", ModuleCategory.player) {
    private val delayNextShift = SliderSetting("Delay until next shift", 0.0, 0.0, 800.0, 10.0)
    private val blocksOnly = TickSetting("Blocks only", true)
    private val disableOnForward = TickSetting("Disable on forward", false)
    private val pitchCheck = TickSetting("Pitch check", false)
    private val shiftValue = TickSetting("Shift", false)

    private var sneaking = false
    private var timestampSafeWalk = 0L

    init {
        addSetting(delayNextShift)
        addSetting(blocksOnly)
        addSetting(disableOnForward)
        addSetting(pitchCheck)
        addSetting(shiftValue)
    }

    override fun onEnable() {
        if (shiftValue.get() && PlayerUtils.playerOverAir()) {
            safeWalk(false)
        }

        sneaking = false
        timestampSafeWalk = 0L
    }

    @SubscribeEvent
    fun onTick(event: PlayerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            if (shiftValue.get() && PlayerUtils.isPlayerInGame) {
                if (mc.thePlayer.onGround && PlayerUtils.playerOverAir()) {
                    if (blocksOnly.get()) {
                        val heldItem = mc.thePlayer.heldItem
                        if (heldItem == null || heldItem.item !is ItemBlock) {
                            safeWalk(false)
                            return
                        }
                    }

                    if (disableOnForward.get() && Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode)) {
                        safeWalk(false)
                        return
                    }

                    if (pitchCheck.get() && mc.thePlayer.rotationPitch <= 70f) {
                        safeWalk(false)
                        return
                    }

                    safeWalk(true)
                } else if (sneaking) {
                    safeWalk(false)
                }

                if (sneaking && mc.thePlayer.capabilities.isFlying)
                    safeWalk(false)
            }
        }
    }
    
    fun safeWalk(canShift: Boolean) {
        if (!sneaking) {
            if (canShift) return
        } else if (!canShift) return

        if (canShift) {
            val delay = delayNextShift.get().toInt().toLong()
            if (delay != 0L) {
                if (System.currentTimeMillis() - timestampSafeWalk <= delay) return
                timestampSafeWalk = System.currentTimeMillis()
            }
        } else {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode)) return
            timestampSafeWalk = System.currentTimeMillis()
        }

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, canShift)
        sneaking = canShift
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (shiftValue.get() && event.gui == null) {
            sneaking = mc.thePlayer.isSneaking
        }
    }

}