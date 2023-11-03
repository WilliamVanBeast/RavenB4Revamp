package net.minusmc.ravenb4.module.modules.render

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import org.lwjgl.input.Keyboard

class Gui: Module("Gui", ModuleCategory.render, keyCode = Keyboard.KEY_RSHIFT) {

    override fun onEnable() {
        toggleModule()
        mc.displayGuiScreen(RavenB4.clickGui)
    }

}