package net.minusmc.ravenb4.setting

import com.google.gson.JsonElement
import com.google.gson.JsonObject

abstract class Setting<T> (val name: String, protected var value: T) {
    protected val defaultValue = value

    fun get() = value

    open fun set(newValue: T) {
        this.value = newValue
    }

    open fun resetToDefaults() {
        this.value = defaultValue
    }

    abstract fun getConfigAsJson(): JsonElement

    abstract fun applyConfigFromJson(data: JsonObject)

    //open fun createComponent(moduleComponent: ModuleComponent?): Component?
}