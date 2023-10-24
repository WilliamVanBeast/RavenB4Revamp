package raven;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class Reach extends gs {
    private final Option minReach = new DoubleOption("Min Reach", 3.1, 3.0, 6.0, 0.05);
    private final Option maxReach = new DoubleOption("Max Reach", 3.3, 3.0, 6.0, 0.05);
    private final Option weaponOnly = new BooleanOption("Weapon Only", false);
    private final Option movingOnly = new BooleanOption("Moving Only", false);
    private final Option sprintOnly = new BooleanOption("Sprint Only", false);
    private final Option hitThroughBlocks = new BooleanOption("Hit Through Blocks", false);

    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        if (event.button < 0 || !event.buttonstate) {
            return;
        }

        if (isConditionsMet()) {
            performReachAction();
        }
    }

    private boolean isConditionsMet() {
        return !(weaponOnly.isEnabled() && isPlayerHoldingWeapon()) &&
               !(movingOnly.isEnabled() && isPlayerNotMoving()) &&
               !(sprintOnly.isEnabled() && !isPlayerSprinting()) &&
               !(hitThroughBlocks.isEnabled() && isHittingBlock());
    }

    private boolean isPlayerHoldingWeapon() {
        return player.getHeldItem() != null; // You need to replace this with the correct check
    }

    private boolean isPlayerNotMoving() {
        return player.motionX == 0.0D && player.motionZ == 0.0D;
    }

    private boolean isPlayerSprinting() {
        return player.isSprinting();
    }

    private boolean isHittingBlock() {
        BlockPos blockPos = player.rayTrace(200, 1.0F).getBlockPos();
        return blockPos != null && world.getBlockState(blockPos).getBlock() != Blocks.AIR;
    }

    private void performReachAction() {
        double minDistance = minReach.getValue();
        double maxDistance = maxReach.getValue();
        Object[] hitResult = rayTraceEntities(minDistance, 0.0D);

        if (hitResult != null) {
            Entity target = (Entity) hitResult[0];
            Vec3 hitVector = (Vec3) hitResult[1];

            player.rayTrace(200, 1.0F).hitEntity(target, hitVector);
        }
    }

    private Object[] rayTraceEntities(double distance, double offset) {
        if (!isThirdPerson()) {
            distance = isSneaking() ? 3.0D : 6.0D;
        }

        Entity viewer = getPlayer();
        Entity target = null;

        Vec3 viewerPosition = viewer.getPositionEyes(1.0F);
        Vec3 viewerLookDirection = viewer.getLook(1.0F);
        Vec3 targetPosition = viewerPosition.add(viewerLookDirection.scale(distance));
        Vec3 hitVector = null;

        List<Entity> entities = world.getEntitiesInAABBexcluding(viewer, viewer.getEntityBoundingBox()
                .offset(viewerLookDirection.x * distance, viewerLookDirection.y * distance, viewerLookDirection.z * distance)
                .expand(1.0D, 1.0D, 1.0D));

        double minDistance = distance;

        for (Entity entity : entities) {
            if (entity.isEntityAlive()) {
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                axisAlignedBB = axisAlignedBB.expand(offset, offset, offset);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(viewerPosition, targetPosition);

                if (axisAlignedBB.isVecInside(viewerPosition)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        target = entity;
                        hitVector = movingObjectPosition == null ? viewerPosition : movingObjectPosition.hitVec;
                        minDistance = 0.0D;
                    }
                } else if (movingObjectPosition != null) {
                    double distanceToEntity = viewerPosition.distanceTo(movingObjectPosition.hitVec);
                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        if (entity == viewer.getRidingEntity()) {
                            if (minDistance == 0.0D) {
                                target = entity;
                                hitVector = movingObjectPosition.hitVec;
                            }
                        } else {
                            target = entity;
                            hitVector = movingObjectPosition.hitVec;
                            minDistance = distanceToEntity;
                        }
                    }
                }
            }
        }

        if (minDistance < distance && !(target instanceof EntityLivingBase) && !(target instanceof EntityItemFrame)) {
            target = null;
        }

        return (target != null && hitVector != null) ? new Object[]{target, hitVector} : null;
    }
}
