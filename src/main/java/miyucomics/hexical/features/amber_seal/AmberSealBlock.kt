package miyucomics.hexical.features.amber_seal

import miyucomics.hexical.inits.HexicalSounds
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Blocks
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.sound.SoundCategory
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

object AmberSealBlock : BlockWithEntity(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)) {
	override fun onBreak(world: World, pos: BlockPos, state: BlockState?, player: PlayerEntity) {
		val seal = world.getBlockEntity(pos)
		if (!player.isCreative || seal !is AmberSealBlockEntity) {
			super.onBreak(world, pos, state, player)
			return
		}

		val drop = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, seal.toItem())
		drop.setToDefaultPickupDelay()
		world.spawnEntity(drop)
		super.onBreak(world, pos, state, player)
	}

	override fun getDroppedStacks(state: BlockState?, builder: LootContextParameterSet.Builder): MutableList<ItemStack?> {
		val list = super.getDroppedStacks(state, builder)
		val seal = builder.getOptional(LootContextParameters.BLOCK_ENTITY)
		if (seal is AmberSealBlockEntity)
			list.add(seal.toItem())
		return list
	}

	override fun onUse(state: BlockState, world: World, position: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
		val seal = world.getBlockEntity(position) as? AmberSealBlockEntity ?: return ActionResult.PASS
		val newState = seal.encasedState
		DanglingState.queuedBlockEntity = seal.encasedEntity
		world.setBlockState(position, newState, NOTIFY_ALL or REDRAW_ON_MAIN_THREAD)
		world.playSound(player, position, HexicalSounds.MAGIC_WOOSHES, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f)
		return ActionResult.success(world.isClient)
	}

	override fun getRenderType(state: BlockState) = BlockRenderType.ENTITYBLOCK_ANIMATED
	override fun isTransparent(state: BlockState, world: BlockView, pos: BlockPos) = true
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = AmberSealBlockEntity(pos, state)

	fun copyRotationParameters(original: BlockState, reference: BlockState): BlockState {
		var output = copyPropertyIfPresent(original, reference, Properties.HORIZONTAL_FACING)
		output = copyPropertyIfPresent(output, reference, Properties.FACING)
		output = copyPropertyIfPresent(output, reference, Properties.HORIZONTAL_AXIS)
		output = copyPropertyIfPresent(output, reference, Properties.AXIS)
		output = copyPropertyIfPresent(output, reference, Properties.ATTACHMENT)
		output = copyPropertyIfPresent(output, reference, Properties.HOPPER_FACING)
		return output
	}

	private fun <T : Comparable<T>> copyPropertyIfPresent(original: BlockState, reference: BlockState, property: Property<T>): BlockState {
		if (original.contains(property) && reference.contains(property))
			return original.with(property, reference.get(property))
		return original
	}
}