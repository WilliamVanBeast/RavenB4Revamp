package net.minusmc.ravenb4.module

import net.minecraft.client.gui.FontRenderer
import net.minusmc.ravenb4.module.modules.combat.*
import net.minusmc.ravenb4.module.modules.funs.*
import net.minusmc.ravenb4.module.modules.movement.*
import net.minusmc.ravenb4.module.modules.others.*
import net.minusmc.ravenb4.module.modules.player.DelayRemover
import net.minusmc.ravenb4.module.modules.render.*
import net.minusmc.ravenb4.module.modules.world.*

class ModuleManager {
    val modules = mutableListOf<Module>()
    val moduleClassMap = hashMapOf<Class<*>, Module>()

    init {
        //Combat
        addModule(AntiKnockback())
        addModule(Reach())

        //Movement
        addModule(SlyPort())

        //World
        addModule(BreakProgress())
        addModule(Scaffold2())

        //Player
        addModule(DelayRemover())

        //Render
        addModule(NameTags())
        addModule(Gui())

        //Other
        addModule(FakeChat())

        //Fun
        addModule(Spin())

        modules.sortedBy { it.name }
    }

    fun addModule(module: Module) {
        modules.add(module)
        moduleClassMap[module.javaClass] = module
    }

    fun getModulesInCategory(category: ModuleCategory) = modules.filter {it.category == category}

    fun getLongestActiveModule(fontRenderer: FontRenderer) = modules.filter { it.enabled }.maxByOrNull { fontRenderer.getStringWidth(it.name) }

    fun getBoxHeight(fontRenderer: FontRenderer, margin: Int) = modules.filter { it.enabled }.sumOf { fontRenderer.FONT_HEIGHT + margin }

    operator fun <T: Module> get(clazz: Class<T>): T? = moduleClassMap[clazz] as T?
}