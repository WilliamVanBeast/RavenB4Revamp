package net.minusmc.ravenb4.module

import net.minecraft.client.gui.FontRenderer
import net.minusmc.ravenb4.utils.MinecraftInstance
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
    val enModulesList = mutableListOf<Module>()
    val moduleClassMap = hashMapOf<Class<*>, Module>()
    
    lateinit var autoClicker: AutoClicker
    lateinit var antiShuffle: AntiShuffle
    lateinit var nameHider: NameHider
    lateinit var noSlow: NoSlow
    lateinit var safeWalk: SafeWalk
    lateinit var keepSprint: KeepSprint
    lateinit var reach: Reach
    lateinit var hud: HUD

    init {
        //Combat
        autoClicker = AutoClicker()
        addModule(autoClicker)
        addModule(AntiKnockback())
        addModule(KillAura())
        reach = Reach()
        addModule(reach)

        //Movement
        addModule(SlyPort())
        keepSprint = KeepSprint()
        addModule(keepSprint)
        noSlow = NoSlow()
        addModule(noSlow)

        //World
        addModule(BreakProgress())
        addModule(Scaffold2())

        //Player
        addModule(DelayRemover())
        safeWalk = SafeWalk()
        addModule(safeWalk)
        addModule(VClip())

        //Render
        antiShuffle = AntiShuffle()
        addModule(antiShuffle)
        addModule(NameTags())
        addModule(Gui())
        hud = HUD()
        addModule(hud)

        //Other
        addModule(FakeChat())
        nameHider = NameHider()
        addModule(nameHider)

        //Fun
        addModule(Spin())

        //Minigames
        addModule(SumoFences())
        addModule(DuelsStats())

        //antiBot.enable()
    }

    fun addModule(module: Module) {
        modules.add(module)
        moduleClassMap[module.javaClass] = module
    }

    fun getModulesInCategory(category: ModuleCategory) = modules.filter {it.category == category}

    fun getLongestActiveModule(fontRenderer: FontRenderer) = modules.filter { it.toggled }.maxByOrNull { fontRenderer.getStringWidth(it.name) }

    fun getBoxHeight(fontRenderer: FontRenderer, margin: Int) = modules.filter { it.toggled }.sumOf { fontRenderer.FONT_HEIGHT + margin }

    fun <T: Module> getModule(clazz: Class<T>): T? = moduleClassMap[clazz] as T?

    fun sort() {
        if (hud.alphabetSort.get())
            enModulesList.sortWith (compareBy {it.name})
        else
            enModulesList.sortWith (compareBy {-MinecraftInstance.mc.fontRendererObj.getStringWidth(it.name)})
    }

    operator fun <T: Module> get(clazz: Class<T>): T? = getModule(clazz)
    
}