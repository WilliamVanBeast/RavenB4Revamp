package raven;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Scaffold2 extends gs {
    public static fe bestWhileMovingForward;
    public static et allowSprinting;
    public static gr highlightBlock;
    public static et placeOnPost;
    public static et safeWalk;
    public static et silentSwing;
    public static fe motion;
    public static fe placeDelay;
    public static et tower;
    public static fe towerSpeed;

    private static float scaffoldOffset;
    private static MovingObjectPosition lastBlockHit;
    private static boolean isSprinting = false;
    private static long lastPlacementTime = 0;

    public Scaffold2() {
        super("Scaffold2", y.i, 0);
        this.addSetting(bestWhileMovingForward = new gr("Best while moving forward."));
        this.addSetting(allowSprinting = new et("Allow sprinting", false));
        this.addSetting(highlightBlock = new et("Highlight block", false));
        this.addSetting(placeOnPost = new et("Place on post", false));
        this.addSetting(safeWalk = new et("Safewalk", true));
        this.addSetting(silentSwing = new et("Silent swing", false));
        this.addSetting(motion = new fe("Motion", 1.0D, 0.2D, 1.0D, 0.01D));
        this.addSetting(placeDelay = new fe("Place delay", 50.0D, 50.0D, 250.0D, 10.0D));
        this.addSetting(tower = new et("Tower", false));
        this.addSetting(towerSpeed = new fe("Tower speed", 0.42D, 0.36D, 0.8D, 0.01D));
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (shouldRender()) {
            HashMap<BlockPos, EnumFacing> blocksToRender = findBlocksToRender();

            for (Entry<BlockPos, EnumFacing> entry : blocksToRender.entrySet()) {
                renderBlock(entry.getKey().offset(entry.getValue()), Color.yellow.getRGB(), true, false);
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (event.buttonstate && isSprinting) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void tower(RenderWorldLastEvent event) {
        if (shouldTower() && isSprinting) {
            handleTower();
        }
    }

    private boolean shouldRender() {
        return highlightBlock.isEnabled() && isPlayerOnGround() && shouldHighlight();
    }

    private boolean shouldTower() {
        return tower.isEnabled() && isSprinting;
    }

    private boolean isPlayerOnGround() {
        return mc.player.onGround;
    }

    private boolean shouldHighlight() {
        return (lastBlockHit != null && canPlaceBlock(lastBlockHit)) || tower.isEnabled();
    }

    private boolean canPlaceBlock(MovingObjectPosition blockHit) {
        ItemStack itemStack = mc.player.getHeldItemMainhand();
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            BlockPos pos = blockHit.getBlockPos();
            lastPlacementTime = System.currentTimeMillis();
            if (mc.playerController.processRightClickBlock(mc.player, mc.world, itemStack, pos, blockHit.sideHit, blockHit.hitVec)) {
                if (silentSwing.isEnabled()) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                } else {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.onUpdate();
                }
                return true;
            }
        }
        return false;
    }

    private HashMap<BlockPos, EnumFacing> findBlocksToRender() {
        HashMap<BlockPos, EnumFacing> blocksToRender = new HashMap<>();
        int yOffset = mc.player.onGround ? -1 : -2;
        int searchRadius = 2;

        for (int y = yOffset; y < 0; ++y) {
            for (int x = -searchRadius; x <= searchRadius; ++x) {
                for (int z = -searchRadius; z <= searchRadius; ++z) {
                    BlockPos pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
                    if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) {
                        EnumFacing facing = null;
                        double distance = 0.0D;

                        for (EnumFacing side : EnumFacing.VALUES) {
                            if (side != EnumFacing.DOWN && (side != EnumFacing.UP || !mc.player.onGround && mc.player.motionY >= 0.0D)) {
                                BlockPos offsetPos = pos.offset(side);
                                if (mc.world.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                                    double distanceToBlock = offsetPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ);
                                    if (facing == null || distanceToBlock < distance) {
                                        facing = side;
                                        distance = distanceToBlock;
                                    }
                                }
                            }
                        }

                        if (facing != null) {
                            blocksToRender.put(pos, facing);
                        }
                    }
                }
            }
        }
        return blocksToRender;
    }

    private void renderBlock(BlockPos pos, int color, boolean outline, boolean fill) {
        // Implement the rendering logic for blocks here
    }

    private void handleTower() {
        // Implement the tower behavior here
    }
}
