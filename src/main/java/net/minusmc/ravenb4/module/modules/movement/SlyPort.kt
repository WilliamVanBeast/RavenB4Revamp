package net.minusmc.ravenb4.module.modules.movement

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

class SlyPort : Module("SlyPort", ModuleCategory.movement, "tp behind enemies") {
    private val s = false

    var desc: DescriptionSetting = DescriptionSetting("Desc", "Teleport behind enemies.");
    var range: SliderSetting = SliderSetting("Range", 6.0, 2.0, 15.0, 1.0)
    var playSound: TickSetting = TickSetting("Play sound", true)
    var aimSetting: TickSetting = TickSetting("Aim", true)
    var playerOnly: TickSetting = TickSetting("Players only", true)

    init {
        registerSetting(desc)
        registerSetting(range)
        registerSetting(aimSetting)
        registerSetting(playSound)
        registerSetting(playerOnly)
    }

    override fun onEnable() {
        val en = ge()
        if (en != null) {
            tp(en)
        }
        disable()
    }

    private fun tp(en: Entity) {
        if (playSound.get()) {
            mc.thePlayer.playSound("mob.endermen.portal", 1.0f, 1.0f)
        }
        val vec = en.lookVec
        val x = en.posX - vec.xCoord * 2.5
        val z = en.posZ - vec.zCoord * 2.5
        mc.thePlayer.setPosition(x, mc.thePlayer.posY, z)
        if (aimSetting.get()) {
            //Utils.Player.aim(en, 0.0f, false)
        }
    }

    private fun ge(): Entity? {
        //Later
        return null;
    }
}
