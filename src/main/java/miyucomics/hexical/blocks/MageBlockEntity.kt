package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.spell.getPositiveInt
import at.petrak.hexcasting.api.spell.getPositiveIntUnder
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import java.util.*

class MageBlockEntity(pos: BlockPos, state: BlockState) : HexBlockEntity(HexicalBlocks.MAGE_BLOCK_ENTITY, pos, state) {
	var properties: MutableMap<String, Boolean> = mutableMapOf(
		"bouncy" to false,
		"energized" to false,
		"ephemeral" to false,
		"invisible" to false,
		"replaceable" to false,
		"semipermeable" to false,
		"volatile" to false
	)
	var redstone: Int = 0
	var lifespan: Int = 0
	var canPass: UUID = UUID.randomUUID()

	override fun saveModData(tag: NbtCompound) {
		properties.forEach { (key, value) -> tag.putBoolean(key, value) }
		tag.putInt("lifespan", this.lifespan)
		tag.putInt("redstone", this.redstone)
		tag.putUuid("canPass", this.canPass)
	}

	override fun loadModData(tag: NbtCompound) {
		properties.keys.forEach { key -> properties[key] = tag.getBoolean(key) }
		this.lifespan = tag.getInt("lifespan")
		this.redstone = tag.getInt("redstone")
		this.canPass = tag.getUuid("canPass")
	}

	fun setProperty(property: String, args: List<Iota>, caster: ServerPlayerEntity) {
		if (property == "energized")
			this.redstone = args.getPositiveIntUnder(0, 16, args.size)
		if (property == "ephemeral")
			this.lifespan = args.getPositiveInt(0, args.size)
		if (property == "semipermeable")
			this.canPass = caster.uuid
		properties[property] = !properties[property]!!
		sync()
	}
}