package net.minusmc.ravenb4.module

import net.minecraft.client.gui.FontRenderer
import net.minusmc.ravenb4.module.modules.combat.*
import net.minusmc.ravenb4.module.modules.funs.*
import net.minusmc.ravenb4.module.modules.movement.*
import net.minusmc.ravenb4.module.modules.others.*
import net.minusmc.ravenb4.module.modules.player.*
import net.minusmc.ravenb4.module.modules.render.*
import net.minusmc.ravenb4.module.modules.world.*
import net.minusmc.ravenb4.module.modules.minigames.*

class ModuleManager {
    val modules = mutableListOf<Module>()
    val moduleClassMap = hashMapOf<Class<*>, Module>()

    init {
        //Combat
        addModule(AutoClicker())
        addModule(AntiKnockback())
        addModule(KillAura())
        addModule(Reach())

        //Movement
        addModule(SlyPort())
        addModule(KeepSprint())
        addModule(NoSlow())

        //World
        addModule(BreakProgress())
        addModule(Scaffold2())

        //Player
        addModule(DelayRemover())
        addModule(SafeWalk())
        addModule(VClip())

        //Render
        addModule(NameTags())
        addModule(Gui())

        //Other
        addModule(FakeChat())
        addModule(NameHider())

        //Fun
        addModule(Spin())

        //Minigames
        addModule(SumoFences())
        addModule(DuelsStats())

        modules.sortedBy { it.name }
    }

    fun addModule(module: Module) {
        modules.add(module)
        moduleClassMap[module.javaClass] = module
    }

    fun getModulesInCategory(category: ModuleCategory) = modules.filter {it.category == category}

    fun getLongestActiveModule(fontRenderer: FontRenderer) = modules.filter { it.enabled }.maxByOrNull { fontRenderer.getStringWidth(it.name) }

    fun getBoxHeight(fontRenderer: FontRenderer, margin: Int) = modules.filter { it.enabled }.sumOf { fontRenderer.FONT_HEIGHT + margin }

    fun <T: Module> getModule(clazz: Class<T>): T? = moduleClassMap[clazz] as T?

    operator fun <T: Module> get(clazz: Class<T>): T? = getModule(clazz)
    
}