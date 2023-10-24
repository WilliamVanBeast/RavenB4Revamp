package net.minusmc.ravenb4.module

import net.minecraft.client.gui.FontRenderer

object ModuleManager {
    val modules = mutableListOf<Module>()

    init {
        // maybe use resolverpackage
        modules.sortedBy { it.name }
    }

    fun addModule(module: Module) = modules.add(module)

    fun getModulesInCategory(category: ModuleCategory) = modules.filter {it.category == category}

    fun getLongestActiveModule(fontRenderer: FontRenderer) = modules.filter { it.isEnabled() }.maxByOrNull { fontRenderer.getStringWidth(it.name) }

    fun getBoxHeight(fontRenderer: FontRenderer, margin: Int) = modules.filter { it.isEnabled() }.sumOf { fontRenderer.FONT_HEIGHT + margin }
}