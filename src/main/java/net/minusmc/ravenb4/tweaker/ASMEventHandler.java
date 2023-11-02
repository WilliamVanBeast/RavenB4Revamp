package net.minusmc.ravenb4.tweaker;

import net.minusmc.ravenb4.RavenB4;
import net.minusmc.ravenb4.module.Module;
import net.minusmc.ravenb4.module.modules.combat.AutoClicker;
import net.minusmc.ravenb4.module.modules.combat.Reach;
import net.minusmc.ravenb4.module.modules.movement.KeepSprint;
import net.minusmc.ravenb4.module.modules.movement.NoSlow;
import net.minusmc.ravenb4.module.modules.others.NameHider;
import net.minusmc.ravenb4.module.modules.player.SafeWalk;
import net.minusmc.ravenb4.module.modules.render.AntiShuffle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public class ASMEventHandler {
   private static final Minecraft mc = Minecraft.getMinecraft();

   /**
    * called when Minecraft format text
    * ASM Modules : NameHider, AntiShuffle
    */
   public static String getUnformattedTextForChat(String s) {
      NameHider nameHider = RavenB4.moduleManager.getModule(NameHider.class);
      if (nameHider != null && nameHider.getEnabled()) {
         s = nameHider.getUnformattedTextForChat(s);
      }

      AntiShuffle antiShuffle = RavenB4.moduleManager.getModule(AntiShuffle.class);
      if (antiShuffle != null && antiShuffle.getEnabled()) {
         s = antiShuffle.getUnformattedTextForChat(s);
      }

      return s;
   }


   /**
    * called when an entity moves
    * ASM Modules : SafeWalk
    */
   public static boolean onEntityMove(Entity entity) {
      if (entity == mc.thePlayer && mc.thePlayer.onGround) {
         SafeWalk safeWalk = RavenB4.moduleManager.getModule(SafeWalk.class);

         if (safeWalk != null && safeWalk.getEnabled() && !safeWalk.getShiftValue().get()) {
            if (safeWalk.getBlocksOnly().get()) {
               ItemStack i = mc.thePlayer.getHeldItem();
               if (i == null || !(i.getItem() instanceof ItemBlock)) {
                  return mc.thePlayer.isSneaking();
               }
            }

            return true;
         } else {
            return mc.thePlayer.isSneaking();
         }
      } else {
         return false;
      }
   }

   /*public String getModName()
   {
      return "lunarclient:db2533c";
   }*/

   /**
    * called when a player is using an item (aka right-click)
    * ASM Modules : NoSlow
    */
   public static void onLivingUpdate() {
      NoSlow noSlow = RavenB4.moduleManager.getModule(NoSlow.class);
      if (noSlow != null && noSlow.getEnabled()) {
         noSlow.doNoSlow();
      } else {
         mc.thePlayer.movementInput.moveStrafe *= 0.2F;
         mc.thePlayer.movementInput.moveForward *= 0.2F;
      }
   }

   /**
    * called when a player is moving and hits another one
    * ASM Modules : KeepSprint
    */
   public static void onAttackTargetEntityWithCurrentItem(Entity en) {
      KeepSprint keepSprint = RavenB4.moduleManager.getModule(KeepSprint.class);
      if (keepSprint != null && keepSprint.getEnabled()) {
         keepSprint.doKeepSprint();
      } else {
         mc.thePlayer.motionX *= 0.6D;
         mc.thePlayer.motionZ *= 0.6D;
      }
   }

   /**
    * called every tick
    * ASM Modules : AutoClicker, Reach
    */
   public static void onTick() {
      AutoClicker autoClicker = RavenB4.moduleManager.getModule(AutoClicker.class);
      Reach reach = RavenB4.moduleManager.getModule(Reach.class);
      if (autoClicker == null || !autoClicker.getEnabled() || !Mouse.isButtonDown(0) || !reach.canReach()) {
         mc.entityRenderer.getMouseOver(1.0F);
      }
   }
}
