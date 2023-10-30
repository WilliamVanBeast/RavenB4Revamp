package net.minusmc.ravenb4.module.modules.others

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

class NameHider: Module("NameHider", ModuleCategory.other) {
    private val hideAllNameValue = TickSetting("Hide all names", false)
    val enemyName = "raven"
    val yourName = "You"

    init {
        addSetting(DescriptionSetting("Desc", "Command: cname [name]"))
        addSetting(hideAllNameValue)
    }

    fun getUnformattedTextForChat(s: String): String {
        mc.thePlayer ?: return s

        //return if ()
    }
}