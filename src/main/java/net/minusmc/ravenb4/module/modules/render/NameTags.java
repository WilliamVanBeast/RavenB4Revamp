package raven;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class NameTags extends BaseRenderer {
    private final Option yOffset = new DoubleOption("Y-Offset", 0.0, -40.0, 40.0, 1.0);
    private final Option scale = new DoubleOption("Scale", 1.0, 0.5, 5.0, 0.1);
    private final Option autoScale = new BooleanOption("Auto-scale", false);
    private final Option drawRect = new BooleanOption("Draw Rect", true);
    private final Option showHealth = new BooleanOption("Show Health", true);
    private final Option showHitsToKill = new BooleanOption("Show Hits to Kill", false);
    private final Option showInvis = new BooleanOption("Show Invisibility", true);
    private final Option removeTags = new BooleanOption("Remove Tags", false);
    private final Option renderFriends = new BooleanOption("Render Friends (Green)", false);
    private final Option renderEnemies = new BooleanOption("Render Enemies (Red)", false);

    @SubscribeEvent
    public void renderNameTags(Pre event) {
        EntityPlayer target = event.entity;

        if (shouldRenderNametag(target)) {
            renderNametag(target, event.x, event.y, event.z);
        }
    }

    private boolean shouldRenderNametag(EntityPlayer player) {
        if (removeTags.isEnabled()) {
            return false;
        }

        if (autoScale.isEnabled()) {
            setAutoScale(player);
        } else {
            setManualScale();
        }

        return true;
    }

    private void setAutoScale(EntityPlayer player) {
        double distance = getDistanceToPlayer(player);
        double autoScaleFactor = calculateAutoScale(distance);
        double yScaleOffset = 0.0;
        setScale(autoScaleFactor, yScaleOffset);
    }

    private double getDistanceToPlayer(EntityPlayer player) {
        double xDiff = player.posX - player.prevPosX;
        double yDiff = player.posY - player.prevPosY;
        double zDiff = player.posZ - player.prevPosZ;
        return MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    private double calculateAutoScale(double distance) {
        // Add your auto-scaling logic here
        double scale = 1.0; // Replace with your calculation
        return Math.max(0.5, scale);
    }

    private void setManualScale() {
        double userScale = scale.getValue();
        double yScaleOffset = yOffset.getValue();
        setScale(userScale, yScaleOffset);
    }

    private void setScale(double scaleFactor, double yScaleOffset) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, player.getEyeHeight() + 0.5F + (float) yScaleOffset, 0.0F);
        GlStateManager.scale(-scaleFactor, -scaleFactor, scaleFactor);
    }

    private void renderNametag(EntityPlayer player, double x, double y, double z) {
        String playerName = getPlayerName(player);

        if (drawRect.isEnabled()) {
            drawBackgroundRect(x, y, z);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + player.getEyeHeight() + 0.5F, z);
        GlStateManager.rotate(-player.rotationYaw, 0.0F, 1.0F, 0.0F);

        renderNameText(playerName, x, y, z);

        GlStateManager.popMatrix();
    }

    private String getPlayerName(EntityPlayer player) {
        String playerName = player.getName();

        if (showHealth.isEnabled()) {
            playerName += " " + getFormattedHealth(player);
        }

        if (showHitsToKill.isEnabled()) {
            playerName += " " + getHitsToKill(player);
        }

        if (showInvis.isEnabled()) {
            playerName += " (Invisible)";
        }

        return playerName;
    }

    private String getFormattedHealth(EntityPlayer player) {
        return "Health: " + Math.ceil(player.getHealth());
    }

    private String getHitsToKill(EntityPlayer player) {
        // Calculate the hits to kill the player based on damage and health
        return "Hits to Kill: " + calculateHitsToKill(player);
    }

    private int calculateHitsToKill(EntityPlayer player) {
        // Implement the actual logic to calculate hits to kill
        return 0; // Replace with your calculation
    }

    private void drawBackgroundRect(double x, double y, double z) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        double width = 60.0;
        double height = 8.0;

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x - width / 2, y, z).color(0.0F, 0.0F, 0.0F, 0.7F).endVertex();
        worldRenderer.pos(x - width / 2, y + height, z).color(0.0F, 0.0F, 0.0F, 0.7F).endVertex();
        worldRenderer.pos(x + width / 2, y + height, z).color(0.0F, 0.0F, 0.0F, 0.7F).endVertex();
        worldRenderer.pos(x + width / 2, y, z).color(0.0F, 0.0F, 0.0F, 0.7F).endVertex();

        tessellator.draw();
    }

    private void renderNameText(String playerName, double x, double y, double z) {
        GlStateManager.translate(0.0F, 1.0F, 0.0F);
        GlStateManager.disableLighting();

        int textColor = 0xFFFFFF;
        b.f.drawString(playerName, -b.f.getStringWidth(playerName) / 2, 0, textColor);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }
}
