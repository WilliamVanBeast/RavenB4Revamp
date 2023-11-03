package net.minusmc.ravenb4.module.modules.render

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting
import org.lwjgl.input.Keyboard


class HUD: Module("HUD", ModuleCategory.render) {
    val alphabetSort = TickSetting("Alphabetical sort", false)
}