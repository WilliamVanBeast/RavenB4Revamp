package net.minusmc.ravenb4.module.modules.render

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import org.lwjgl.opengl.GL11
import kotlin.math.ceil
import kotlin.math.max

class NameTags: Module("NameTags", ModuleCategory.render){

    private val yOffset: SliderSetting = SliderSetting("Y-Offset", 0.0, -40.0, 40.0, 1.0)
    private val scale: SliderSetting = SliderSetting("Scale", 1.0, 0.5, 5.0, 0.1)
    private val autoScale: TickSetting = TickSetting("Auto-scale", false)
    private val drawRect: TickSetting = TickSetting("Draw Rect", true)
    private val showHealth: TickSetting = TickSetting("Show Health", true)
    private val showHitsToKill: TickSetting = TickSetting("Show Hits to Kill", false)
    private val showInvis: TickSetting = TickSetting("Show Invisibility", true)
    private val removeTags: TickSetting = TickSetting("Remove Tags", false)
    private val renderFriends: TickSetting = TickSetting("Render Friends (Green)", false)
    private val renderEnemies: TickSetting = TickSetting("Render Enemies (Red)", false)


    init {
        addSetting(yOffset);
        addSetting(scale);
        addSetting(autoScale);
        addSetting(drawRect);
        addSetting(showHealth);
        addSetting(showHitsToKill);
        addSetting(showInvis);
        addSetting(removeTags);
        addSetting(renderFriends);
        addSetting(renderEnemies);
    }

    @SubscribeEvent
    fun renderNameTags(event: RenderLivingEvent.Specials.Pre<*>) {
        val target: EntityLivingBase = event.entity
        if(target !is EntityPlayer) return;
        if (shouldRenderNametag(target)) {
            renderNametag(target, event.x, event.y, event.z)
        }
    }

    private fun renderNametag(player: EntityPlayer, x: Double, y: Double, z: Double) {
        val playerName = getPlayerName(player)
        if (drawRect.get()) {
            drawBackgroundRect(x, y, z)
        }
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y + player.getEyeHeight() + 0.5f, z)
        GlStateManager.rotate(-player.rotationYaw, 0.0f, 1.0f, 0.0f)
        renderNameText(playerName, x, y, z)
        GlStateManager.popMatrix()
    }

    private fun getPlayerName(player: EntityPlayer): String {
        var playerName = player.name
        if (showHealth.get()) {
            playerName += " " + getFormattedHealth(player)
        }
        if (showHitsToKill.get()) {
            playerName += " " + getHitsToKill(player)
        }
        if (showInvis.get()) {
            playerName += " (Invisible)"
        }
        return playerName
    }

    private fun drawBackgroundRect(x: Double, y: Double, z: Double) {
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        val width = 60.0
        val height = 8.0
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
        worldRenderer.pos(x - width / 2, y, z).color(0.0f, 0.0f, 0.0f, 0.7f).endVertex()
        worldRenderer.pos(x - width / 2, y + height, z).color(0.0f, 0.0f, 0.0f, 0.7f).endVertex()
        worldRenderer.pos(x + width / 2, y + height, z).color(0.0f, 0.0f, 0.0f, 0.7f).endVertex()
        worldRenderer.pos(x + width / 2, y, z).color(0.0f, 0.0f, 0.0f, 0.7f).endVertex()
        tessellator.draw()
    }

    private fun renderNameText(playerName: String, x: Double, y: Double, z: Double) {
        GlStateManager.translate(0.0f, 1.0f, 0.0f)
        GlStateManager.disableLighting()
        val textColor = 0xFFFFFF
        mc.fontRendererObj.drawString(playerName, -mc.fontRendererObj.getStringWidth(playerName) / 2, 0, textColor)
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
    }

    private fun getFormattedHealth(player: EntityPlayer): String {
        return "Health: " + ceil(player.health.toDouble())
    }

    private fun getHitsToKill(player: EntityPlayer): String {
        return "Hits to Kill: " + calculateHitsToKill(player)
    }

    private fun calculateHitsToKill(player: EntityPlayer): Int {
        var hits = (player.health / getItemDamage()).toInt();
        if(hits <= 0) hits = 0;
        return hits;
    }

    private fun getItemDamage(): Int {
        if(mc.thePlayer.heldItem == null) return 1;

        return mc.thePlayer.heldItem.itemDamage;
    }

    private fun shouldRenderNametag(player: EntityPlayer): Boolean {
        if (removeTags.get()) {
            return false
        }
        if (autoScale.get()) {
            setAutoScale(player)
        } else {
            setManualScale(player)
        }
        return true
    }

    private fun setAutoScale(player: EntityPlayer) {
        val distance = getDistanceToPlayer(player)
        val autoScaleFactor = calculateAutoScale(distance)
        val yScaleOffset = 0.0
        setScale(player, autoScaleFactor, yScaleOffset)
    }

    private fun getDistanceToPlayer(player: EntityPlayer): Double {
        val xDiff = player.posX - player.prevPosX
        val yDiff = player.posY - player.prevPosY
        val zDiff = player.posZ - player.prevPosZ
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff).toDouble()
    }

    private fun calculateAutoScale(distance: Double): Double {
        // Add your auto-scaling logic here
        val scale = 1.0 // Replace with your calculation
        return max(0.5, scale)
    }

    private fun setManualScale(player: EntityPlayer) {
        val userScale: Double = scale.get()
        val yScaleOffset: Double = yOffset.get()
        setScale(player, userScale, yScaleOffset)
    }

    private fun setScale(player: EntityPlayer, scaleFactor: Double, yScaleOffset: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(0.0f, player.getEyeHeight() + 0.5f + yScaleOffset.toFloat(), 0.0f)
        GlStateManager.scale(-scaleFactor, -scaleFactor, scaleFactor)
    }

}