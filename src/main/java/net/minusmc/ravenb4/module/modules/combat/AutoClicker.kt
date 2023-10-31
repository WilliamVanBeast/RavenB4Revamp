package net.minusmc.ravenb4.module.modules.combat

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.utils.PlayerUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class AutoClicker: Module("AutoClicker", ModuleCategory.combat) {
    private val minCPS = SliderSetting("MinCPS", 9.0, 1.0, 20.0, 0.5)
    private val maxCPS = SliderSetting("MaxCPS", 12.0, 1.0, 20.0, 0.5)
    private val jitter = SliderSetting("Jitter", 0.0, 0.0, 3.0, 0.1)
    private val blockHitChance = SliderSetting("Block hit chance", 0.0, 0.0, 100.0, 1.0)
    val leftClicker = TickSetting("Left click", true)
    val rightClicker = TickSetting("Right click", false)
    val breakBlocks = TickSetting("Break blocks", false)
    val inventoryFill = TickSetting("Inventory full", false)
    val weaponOnly = TickSetting("Weapon only", false)
    val blocksOnly = TickSetting("Blocks only", false)

    private lateinit var playerMouseInput: Method

    private var leftDownTime = 0L
    private var leftUpTime = 0L
    private var leftn = false
    init {
        addSetting(DescriptionSetting("Auto Clicker Desc", "Best with delay remover."))
        addSetting(minCPS)
        addSetting(maxCPS)
        addSetting(jitter)
        addSetting(blockHitChance)
        addSetting(leftClicker)
        addSetting(rightClicker)
        addSetting(breakBlocks)
        addSetting(inventoryFill)
        addSetting(weaponOnly)
        addSetting(blocksOnly)

        try {
            playerMouseInput = GuiScreen::class.java.getDeclaredMethod("func_73864_a", Integer.TYPE, Integer.TYPE, Integer.TYPE)
        } catch (_: Exception) {
            try {
                playerMouseInput = GuiScreen::class.java.getDeclaredMethod("mouseClicked", Integer.TYPE, Integer.TYPE, Integer.TYPE)
            } catch (_: Exception) {}
        }

        if (playerMouseInput != null) playerMouseInput.isAccessible = true
    }

    @SubscribeEvent
    fun onRenderTick(event: RenderTickEvent) {
        if (event.phase == TickEvent.Phase.END && PlayerUtils.isPlayerInGame) {
            if (mc.currentScreen == null && mc.inGameHasFocus) {
                val isWeapon = PlayerUtils.isWeapon()
                if (!isWeapon && mc.thePlayer.isEating) return

                if (leftClicker.get() && Mouse.isButtonDown(0)) {
                    if (weaponOnly.get() && !isWeapon) return
                    // ham j do
                } else if (rightClicker.get() && Mouse.isButtonDown(1)) {
                    if (blocksOnly.get() && (mc.thePlayer.currentEquippedItem != null || mc.thePlayer.currentEquippedItem.item !is ItemBlock)) return
                    // ham j do
                } else {
                    leftDownTime = 0
                    leftUpTime = 0
                }
            } else if (inventoryFill.get() && mc.currentScreen is GuiInventory) {
                if (!Mouse.isButtonDown(0) || !Keyboard.isKeyDown(54) && Keyboard.isKeyDown(42)) {
                    leftDownTime = 0
                    leftUpTime = 0
                } else if (leftDownTime != 0L && leftUpTime != 0L) {
                    if (System.currentTimeMillis() > leftUpTime) {
                        genLeftTimings()
                        inInvClick(mc.currentScreen)
                    }
                }
            }
        }
    }

    fun genLeftTimings() {
        val clickSpeed: Double = Utils.Client.ranModuleVal(leftCPS, this.rand) + 0.4 * this.rand.nextDouble()
        var delay = Math.round(1000.0 / clickSpeed).toInt().toLong()
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && this.rand.nextInt(100) >= 85) {
                this.leftn = true
                this.leftm = 1.1 + this.rand.nextDouble() * 0.15
            } else {
                this.leftn = false
            }
            this.leftk = System.currentTimeMillis() + 500L + this.rand.nextInt(1500) as Long
        }
        if (this.leftn) {
            delay = (delay.toDouble() * this.leftm) as Long
        }
        if (System.currentTimeMillis() > this.leftl) {
            if (this.rand.nextInt(100) >= 80) {
                delay += 50L + this.rand.nextInt(100) as Long
            }
            this.leftl = System.currentTimeMillis() + 500L + this.rand.nextInt(1500) as Long
        }
        leftUpTime = System.currentTimeMillis() + delay
        leftDownTime = System.currentTimeMillis() + delay / 2L - this.rand.nextInt(10) as Long
    }

    private fun inInvClick(guiScreen: GuiScreen) {
        val mouseInGUIPosX = Mouse.getX() * guiScreen.width / mc.displayWidth
        val mouseInGUIPosY = guiScreen.height - Mouse.getY() * guiScreen.height / mc.displayHeight - 1
        try {
            playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0)
        } catch (var5: IllegalAccessException) {
        } catch (var5: InvocationTargetException) {
        }
    }
}