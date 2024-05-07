package miyucomics.hexical.blocks

import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicRecord
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class AkashicTerminalBlock : Block(Settings.copy(Blocks.OAK_LOG).mapColor(MapColor.PURPLE).sounds(BlockSoundGroup.WOOD).strength(2f)) {
	private val searches: List<Vec3i> = listOf(
		Vec3i(-1, 0, 0), Vec3i(1, 0, 0),
		Vec3i(0, -1, 0), Vec3i(0, 1, 0),
		Vec3i(0, 0, -1), Vec3i(0, 0, 1),
	)

	override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		if (world.isClient) {
			return ActionResult.SUCCESS
		}
		if (player.isSneaking) {
			player.sendMessage(Text.literal(PersistentStateHandler.getPlayerBoundLibrary(player).toString()))
		} else {
			for (search in searches) {
				if (world.getBlockState(pos.add(search)).block is BlockAkashicRecord) {
					if (PersistentStateHandler.getPlayerBoundLibrary(player) == null || PersistentStateHandler.getPlayerBoundLibrary(player) == pos.add(search)) {
						PersistentStateHandler.setPlayerBoundLibrary(player, pos)
						player.sendMessage(Text.translatable("hexical.message.library_bound"))
					} else {
						PersistentStateHandler.setPlayerBoundLibrary(player, null)
						player.sendMessage(Text.translatable("hexical.message.library_unbound"))
					}
					return ActionResult.SUCCESS
				}
			}
		}
		return ActionResult.FAIL
	}
}