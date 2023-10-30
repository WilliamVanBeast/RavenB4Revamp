package net.minusmc.ravenb4.module.modules.render

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import org.lwjgl.input.Keyboard


class Gui: Module("Gui", ModuleCategory.render) {

    init {
        setKeyCode(Keyboard.KEY_RSHIFT)
    }

    override fun onEnable() {
        this.setToggled(false)
        mc.displayGuiScreen(RavenB4.clickGui)
    }

}