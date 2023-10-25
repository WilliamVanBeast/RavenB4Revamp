package net.minusmc.ravenb4

import net.minecraftforge.fml.common.Mod
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleManager

enum class RavenB4 {

    INSTANCE;

    var moduleManager: ModuleManager = ModuleManager();

    fun init() {

    }
}