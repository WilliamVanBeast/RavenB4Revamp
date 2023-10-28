package net.minusmc.ravenb4.utils.block

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import kotlin.math.floor
import kotlin.math.pow

class CustomBlockPos(val x: Double, val y: Double, val z: Double) {

    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }

    fun calculateDistance(otherPoint: CustomBlockPos): Double {
        return (otherPoint.x - x).pow(2.0) + (otherPoint.y - y).pow(2.0) + (otherPoint.z - z).pow(2.0)
    }

    fun add(otherPoint: CustomBlockPos): CustomBlockPos {
        return create(x + otherPoint.x, y + otherPoint.y, z + otherPoint.z)
    }

    fun toVec3(): Vec3 {
        return Vec3(x, y, z)
    }

    fun create(x: Double, y: Double, z: Double): CustomBlockPos {
        return CustomBlockPos(this.x + x, this.y + y, this.z + z)
    }

    fun floorValues(): CustomBlockPos {
        return CustomBlockPos(
            floor(x), floor(y), floor(
                z
            )
        )
    }
}