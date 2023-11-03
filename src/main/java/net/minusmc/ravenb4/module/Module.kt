package net.minusmc.ravenb4.module

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.setting.Setting
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.utils.MinecraftInstance
import org.lwjgl.input.Keyboard

open class Module(val name: String, val category: ModuleCategory, var keyCode: Int): MinecraftInstance() {
    constructor(name: String, category: ModuleCategory): this(name, category, 0)

    protected val settings = mutableListOf<Setting<*>>()
    var state = false
    var toggled = false

    fun keybind(): Boolean {
        if (keyCode == 0) return false
        if (!toggled && Keyboard.isKeyDown(keyCode)) {
            toggleModule()
            toggled = false
        } else if (!Keyboard.isKeyDown(keyCode)) {
            toggled = true
        }
        return toggled
    }

    fun register() {
        toggled = true
        RavenB4.moduleManager.enModulesList.add(this)
        if (RavenB4.moduleManager.hud.state) {
            RavenB4.moduleManager.sort()
        }
        MinecraftForge.EVENT_BUS.register(this)
        FMLCommonHandler.instance().bus().register(this)
        onEnable()
    }

    fun unregister() {
        toggled = false
        RavenB4.moduleManager.enModulesList.remove(this)
        MinecraftForge.EVENT_BUS.unregister(this)
        FMLCommonHandler.instance().bus().unregister(this)
        onDisable()
    }

    fun toggleModule() {
        if (toggled) register()
        else unregister()
    }

    open fun onEnable() {

    }

    open fun onDisable() {

    }

    open fun update() {

    }

    open fun guiUpdate() {

    }

    fun addSetting(setting: Setting<*>) {
        settings.add(setting)
    }

}