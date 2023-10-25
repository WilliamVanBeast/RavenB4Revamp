package net.minusmc.ravenb4.module.modules.others

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

object Settings: Module("Settings", ModuleCategory.other) {
    private val generalDescription = DescriptionSetting("General", "General")
    val axeWeapon = TickSetting("Set axe as weapon", false)
    val rodWeapon = TickSetting("Set rod as weapon", false)
    val stickWeapon = TickSetting("Set stick as weapon", false)
    val middleClick = TickSetting("Middle click friends", false)
    private val rotationsDescription = DescriptionSetting("Rotations", "Rotations")
    val rotateBody = TickSetting("Rotate body", true)
    val movementFix = TickSetting("Movement fix", false)
    val patchGCD = TickSetting("Patch GCD", true)
    val limitYawAcceleration = TickSetting("Limit yaw acceleration", false)
    val randomYawValue = SliderSetting("Random yaw factor", 1.0, 0.0, 10.0, 1.0)
    private val profilesDescription = DescriptionSetting("Profiles", "Profiles")
    val disableModulesOnLoad = TickSetting("Disable modules on load", false)
    val sendMessageOnEnable = TickSetting("Send message on load", false)
    private val themeColorsDescription = DescriptionSetting("Theme colors", "Theme colors")
    val offsetValue = SliderSetting("Offset", 0.5, -2.0, 2.0, 0.1)
    val timeMulValue = SliderSetting("Time multiplier", 0.5, 0.1, 4.0, 0.1)
}