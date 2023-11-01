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
import kotlin.random.Random
import kotlin.math.round

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
    private var leftk = 0L
    private var leftl = 0L
    private var leftm = 0L
    private var leftn = false

    private var blocking = false

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

    override fun onDisable() {
        leftDownTime = 0
        leftUpTime = 0
        blocking = false
    }

    @SubscribeEvent
    fun onRenderTick(event: RenderTickEvent) {
        if (event.phase == TickEvent.Phase.END && PlayerUtils.isPlayerInGame) {
            if (mc.currentScreen == null && mc.inGameHasFocus) {
                val isWeapon = PlayerUtils.isWeapon()
                if (!isWeapon && mc.thePlayer.isEating) return

                if (leftClicker.get() && Mouse.isButtonDown(0)) {
                    if (weaponOnly.get() && !isWeapon) return
                    click(mc.gameSettings.keyBindAttack.keyCode, 0)
                } else if (rightClicker.get() && Mouse.isButtonDown(1)) {
                    if (blocksOnly.get() && (mc.thePlayer.currentEquippedItem != null || mc.thePlayer.currentEquippedItem.item !is ItemBlock)) return
                    click(mc.gameSettings.keyBindUseItem.keyCode, 1)
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

    fun click(keyCode: Int, mouseButton: Int) {
        if (breakBlocks.get && mouseButton == 0 && mc.objectMouseOver != null) {
            val blockPos = mc.objectMouseOver.blockPos
            if (blockPos != null) {
                val block = mc.theWorld.getBlockState(blockPos).block
                if (block != Blocks.air && block !is BlockLiquid) {
                    KeyBinding.setKeyBindState(keyCode, true)
                    blocking = true
                    return
                }

                if (blocking) {
                    KeyBinding.setKeyBindState(keyCode, false)
                    blocking = false
                }
            }
        }

        if (jitter.get() > 0.0) {
            val jitterMul = jitter.get().toFloat() * 0.45f
            if (Random.nextBoolean()) 
                mc.thePlayer.rotationYaw += Random.nextFloat() * jitterMul
            else
                mc.thePlayer.rotationYaw -= Random.nextFloat() * jitterMul

            if (Random.nextBoolean())
                mc.thePlayer.rotationPitch += Random.nextFloat() * jitterMul * 0.45f
            else
                mc.thePlayer.rotationPitch -= Random.nextFloat() * jitterMul * 0.45f
        }

        if (leftDownTime > 0 && leftUpTime > 0) {
            if (System.currentTimeMillis() > leftUpTime) {
                KeyBinding.setKeyBindState(keyCode, true)
                KeyBinding.onTick(keyCode)
                genLeftTimings()

            }
        }
    }

    fun genLeftTimings() {
        val clickSpeed: Double = RandomUtils.nextDouble(minCPS, maxCPS) + 0.4 * Random.nextDouble()
        var delay = round(1000.0 / clickSpeed).toLong()

        if (System.currentTimeMillis() > leftk) {

            if (!leftn && Random.nextInt(100) >= 85) {
                leftn = true
                leftm = 1.1 + Random.nextDouble() * 0.15
            } else
                leftn = false

            leftk = System.currentTimeMillis() + 500L + Random.nextInt(1500).toLong()
        }
        if (leftn)
            delay = (delay.toDouble() * leftm).toLong()

        if (System.currentTimeMillis() > leftl) {
            if (Random.nextInt(100) >= 80)
                delay += 50L + Random.nextInt(100).toLong()

            leftl = System.currentTimeMillis() + 500L + Random.nextInt(1500).toLong()
        }

        leftUpTime = System.currentTimeMillis() + delay
        leftDownTime = System.currentTimeMillis() + delay / 2L - Random.nextInt(10).toLong()
    }

    private fun inInvClick(guiScreen: GuiScreen) {
        val mouseInGUIPosX = Mouse.getX() * guiScreen.width / mc.displayWidth
        val mouseInGUIPosY = guiScreen.height - Mouse.getY() * guiScreen.height / mc.displayHeight - 1
        try {
            playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0)
        } catch (e: IllegalAccessException) {
        } catch (e: InvocationTargetException) {
        }
    }
}