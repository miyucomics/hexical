package miyucomics.hexical.blocks

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FlatRightClickImpetusBlock : FlatImpetusBlock() {
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = FlatRightClickImpetusBlockEntity(pos, state)
	override fun onUse(state: BlockState, world: World, pPos: BlockPos, player: PlayerEntity, pHand: Hand, pHit: BlockHitResult): ActionResult {
		if (!player.isSneaking) {
			val tile = world.getBlockEntity(pPos)
			if (tile is FlatRightClickImpetusBlockEntity) {
				if (player is ServerPlayerEntity)
					tile.startExecution(player)
				return ActionResult.SUCCESS
			}
		}
		return ActionResult.PASS
	}
}