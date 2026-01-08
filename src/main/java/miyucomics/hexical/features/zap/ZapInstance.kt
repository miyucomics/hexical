package miyucomics.hexical.features.zap

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

data class ZapInstance(val world: ServerWorld, val position: BlockPos, val power: Int, val time: Int)