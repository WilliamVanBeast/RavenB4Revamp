package net.minusmc.ravenb4

import net.minecraftforge.fml.common.Mod
import net.minusmc.ravenb4.clickgui.raven.ClickGui
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleManager
import java.util.concurrent.Executors

object RavenB4 {

    lateinit var moduleManager: ModuleManager
    lateinit var clickGui: ClickGui

    val executor = Executors.newScheduledThreadPool(2)

    fun init() {
        moduleManager = ModuleManager()
        clickGui = ClickGui()
    }
}