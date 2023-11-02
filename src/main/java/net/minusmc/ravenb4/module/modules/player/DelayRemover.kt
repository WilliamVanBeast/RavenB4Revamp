package net.minusmc.ravenb4.module.modules.player

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.tweaker.MinecraftFields
import java.lang.reflect.Field


class DelayRemover: Module("DelayRemover", ModuleCategory.player) {
    private val hitreg = TickSetting("1.7 hitreg", true)
    private val jumpTicks = TickSetting("Remove jump ticks", false)

    private val leftClickerField = MinecraftFields.leftClickCounter.field
    private val jumpTicksField = MinecraftFields.jumpTicks.field

    init {
        addSetting(hitreg)
        addSetting(jumpTicks)
    }

    @SubscribeEvent
    fun onTickEvent(event: PlayerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            if (hitreg.get()) {
                try {
                    leftClickerField.set(mc, 0)
                } catch (e: IllegalAccessException) {
                } catch (e: IndexOutOfBoundsException) {
                }
            }

            if (jumpTicks.get()) {
                 try {
                    jumpTicksField.set(mc.thePlayer, 0)
                } catch (e: IllegalAccessException) {
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        }
    }
}