package net.minusmc.ravenb4.module.modules.player

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting
import java.lang.reflect.Field


class DelayRemover: Module("DelayRemover", ModuleCategory.player){
    private val hitreg = TickSetting("1.7 hitreg", true)
    private val jumpTicks = TickSetting("Remove jump ticks", false)

    init {
        addSetting(hitreg)
        addSetting(jumpTicks)
    }

    @SubscribeEvent
    fun onTickEvent(playerTickEvent: PlayerTickEvent) {
        if (playerTickEvent.phase != TickEvent.Phase.END) {
            return;
        }

        if(hitreg.get()) {
            val field: Field = Minecraft::class.java.getDeclaredField("leftClickCounter")
            field.setAccessible(true)
            field.set(mc, 0)
        }

        if(jumpTicks.get()) {
            if(mc.gameSettings.keyBindJump.isKeyDown && mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
        }
    }

}