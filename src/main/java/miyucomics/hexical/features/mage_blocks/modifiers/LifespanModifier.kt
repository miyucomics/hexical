package miyucomics.hexical.features.mage_blocks.modifiers

import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.asInt
import miyucomics.hexical.features.mage_blocks.MageBlockModifier
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class LifespanModifier : MageBlockModifier {
	override val id = "lifespan"
	override val argc = 1
	var lifespan = 0

	override fun getArguments(args: List<Iota>) = listOf(args.getInt(1, argc + 1))
	override fun modify(args: List<Any>) { lifespan = args[0] as Int }

	override fun tick(world: ServerWorld, pos: BlockPos, state: BlockState) {
		lifespan--
		if (lifespan <= 0)
			HexicalBlocks.MAGE_BLOCK.onBreak(world, pos, state, null)
	}

	override fun writeNbt(): NbtInt = NbtInt.of(lifespan)
	override fun readNbt(element: NbtElement) { lifespan = element.asInt }
}