package net.minusmc.ravenb4.module.modules.movement

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.module.modules.combat.Reach
import net.minusmc.ravenb4.module.modules.combat.KillAura
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.utils.PlayerUtils

class KeepSprint : Module("KeepSprint", ModuleCategory.movement) {
    var slowValue = SliderSetting("Slow %", 40.0, 0.0, 40.0, 0.5)
    var disableJumpValue = TickSetting("Disable while jumping", true)
    var reduceHitValue = TickSetting("Only reduce reach hits", true)

    init {
        addSetting(DescriptionSetting("Desc", "Default is 40% motion reduction."))
        addSetting(slowValue)
        addSetting(disableJumpValue)
        addSetting(reduceHitValue)
    }

    fun doKeepSprint() {
        var canKeepSprint = true
        if (disableJumpValue.get() && !mc.thePlayer.onGround) {
            canKeepSprint = false
        } else if (reduceHitValue.get() && !mc.thePlayer.capabilities.isCreativeMode) {
            var dist = -1.0
            var eyePos = mc.thePlayer.getPositionEyes(1f)
            val killAura = RavenB4.moduleManager[KillAura::class.java]!!
            if (killAura.state && killAura.target != null) {
                dist = eyePos.distanceTo(killAura.target!!.getPositionEyes(1f))
            } else if (RavenB4.moduleManager[Reach::class.java]!!.state) {
                dist = eyePos.distanceTo(mc.objectMouseOver.hitVec)
            }

            if (dist != -1.0 && dist <= 3.0) canKeepSprint = false
        }

        if (canKeepSprint) {
            val multiplier = (100.0 - slowValue.get().toFloat()) / 100.0
            mc.thePlayer.motionX *= multiplier
            mc.thePlayer.motionZ *= multiplier
        } else {
            mc.thePlayer.motionX *= 0.6
            mc.thePlayer.motionZ *= 0.6
        }
    }
}
