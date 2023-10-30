package net.minusmc.ravenb4.module.modules.others

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.utils.ServerUtils
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
        val string = s
        if (mc.thePlayer != null) {
            string = string.replace(ServerUtils.getName(), yourName)
            val collection = mc.netHandler.getPlayerInfoMap()
            val networkPlayerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer.getUniqueID())
            for (item in collection) {
                if (item.equals(networkPlayerInfo)) continue
                string = string.replace(item.getGameProfile.getName(), enemyName)
            }
        } else string = string.replace(ServerUtils.getName(), enemyName)
        return string
    }
}