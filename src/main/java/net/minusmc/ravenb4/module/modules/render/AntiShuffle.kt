package net.minusmc.ravenb4.module.modules.render

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import org.lwjgl.input.Keyboard


class AntiShuffle: Module("AntiShuffle", ModuleCategory.funs) {
    val obfString = "§k"

    init {
        addSetting(DescriptionSetting("Desc", "Removes obfuscation"))
    }

    fun getUnformattedTextForChat(s: String) = s.replace(obfString, "")

}