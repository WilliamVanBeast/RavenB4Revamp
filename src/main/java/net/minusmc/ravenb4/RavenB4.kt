package net.minusmc.ravenb4

import net.minecraftforge.fml.common.Mod
import net.minusmc.ravenb4.clickgui.raven.ClickGui
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleManager
import java.util.concurrent.Executors
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.Mod.EventHandler

@Mod(modid = "keystrokesmod", name = "KeystrokesMod", version = "KMV5", acceptedMinecraftVersions = "[1.8.9]")
object RavenB4 {

    lateinit var moduleManager: ModuleManager
    lateinit var clickGui: ClickGui

    val executor = Executors.newScheduledThreadPool(2)

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        moduleManager = ModuleManager()
        clickGui = ClickGui()
    }
}