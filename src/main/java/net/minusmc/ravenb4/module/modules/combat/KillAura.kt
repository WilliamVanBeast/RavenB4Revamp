package net.minusmc.ravenb4.module.modules.combat

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import org.apache.commons.lang3.RandomUtils
import net.minecraft.entity.player.EntityPlayer


class KillAura: Module("KillAura", ModuleCategory.combat){
    

    var target: EntityPlayer? = null
}