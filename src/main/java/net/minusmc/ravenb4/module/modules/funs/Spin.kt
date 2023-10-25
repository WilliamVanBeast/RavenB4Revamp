package net.minusmc.ravenb4.module.modules.funs

import net.minecraft.client.entity.EntityPlayerSP
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting


class Spin: Module("Spin", ModuleCategory.funs){
    private val yawSetting = SliderSetting("Rotation yaw", 360.0, 30.0, 360.0, 1.0)
    private val speedSetting = SliderSetting("Speed", 25.0, 1.0, 80.0, 1.0)

    private var yaw = 0f

    init {
        addSetting(yawSetting)
        addSetting(speedSetting)
    }

    //Run through a transformer i suppose?

    fun updateYaw() {
        this.yaw = mc.thePlayer.rotationYaw
    }

    fun spin() {
        val speed: Double = this.yaw + yawSetting.get() - mc.thePlayer.rotationYaw
        if (speed < speedSetting.get()) {
            mc.thePlayer.rotationYaw = (mc.thePlayer.rotationYaw + speed).toFloat()
        } else {
            mc.thePlayer.rotationYaw = (mc.thePlayer.rotationYaw + speedSetting.get()).toFloat()
        }
    }

}