package net.minusmc.ravenb4.module.modules.player

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

class VClip : Module("VClip", ModuleCategory.player) {

    val sendPackets = TickSetting("Send packets", false)
    val distance = SliderSetting("Distance", 3.0, -20.0, 20.0, 0.5)
    val sendMessage = TickSetting("Send message", true)

    init {
        addSetting(distance)
        addSetting(sendMessage)
        addSetting(sendPackets)
    }

    override fun onEnable() {
        val tpDistance = distance.get()
        if (tpDistance != 0.0) {
            if (sendPackets.get()) {
                mc.thePlayer.sendQueue.addToSendQueue(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + tpDistance,
                        mc.thePlayer.posZ,
                        false
                    )
                )
            } else {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + tpDistance, mc.thePlayer.posZ)
            }
            if (sendMessage.get()) {
                //Later i guess
                //gd.bm("&7Teleported you " + (var1 > 0.0D ? "upwards" : "downwards") + " by &b" + var1 + " &7blocks.");
            }
        }
        enabled = false
    }
}
