package net.minusmc.ravenb4

import net.minecraftforge.fml.common.Mod
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleManager

object RavenB4 {

    lateinit var moduleManager: ModuleManager;

    @JvmStatic
    fun init() {
        moduleManager = ModuleManager();
    }
}