package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class MageBlockEntity(pos: BlockPos, state: BlockState) : HexBlockEntity(HexicalBlocks.MAGE_BLOCK_ENTITY, pos, state) {
	var properties: MutableMap<String, Boolean> = mutableMapOf(
		"bouncy" to false,
		"energized" to false,
		"ephemeral" to false,
		"invisible" to false,
		"replaceable" to false,
		"volatile" to false
	)
	var redstone: Int = 0
	var lifespan: Int = 0

	override fun saveModData(tag: NbtCompound) {
		properties.forEach { (key, value) -> tag.putBoolean(key, value) }
		tag.putInt("lifespan", this.lifespan)
		tag.putInt("redstone", this.redstone)
	}

	override fun loadModData(tag: NbtCompound) {
		properties.keys.forEach { key -> properties[key] = tag.getBoolean(key) }
		this.lifespan = tag.getInt("lifespan")
		this.redstone = tag.getInt("redstone")
	}

	fun setProperty(property: String, args: List<Iota>) {
		if (property == "energized")
			this.redstone = args.getPositiveIntUnder(0, 16, args.size)
		if (property == "ephemeral")
			this.lifespan = args.getPositiveInt(0, args.size)
		properties[property] = !properties[property]!!
		sync()
	}
}