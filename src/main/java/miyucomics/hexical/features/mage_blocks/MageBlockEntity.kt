package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class MageBlockEntity(pos: BlockPos, state: BlockState) : HexBlockEntity(HexicalBlocks.MAGE_BLOCK_ENTITY, pos, state) {
	val modifiers: MutableMap<Identifier, MageBlockModifier> = mutableMapOf()

	fun addModifier(modifier: MageBlockModifier) {
		modifiers[modifier.type.id] = modifier
		sync()
	}

	fun clearModifiers() {
		modifiers.clear()
		sync()
	}

	fun <T : MageBlockModifier> getModifier(type: MageBlockModifierType<T>): T = modifiers[type.id] as T
	fun hasModifier(type: MageBlockModifierType<*>) = modifiers.containsKey(type.id)

	override fun saveModData(compound: NbtCompound) {
		compound.putCompound("modifiers", NbtCompound().apply {
			modifiers.forEach {
				put(it.key.toString(), it.value.serialize())
			}
		})
	}

	override fun loadModData(compound: NbtCompound) {
		clearModifiers()
		val modifiers = compound.getCompound("modifiers")
		modifiers.keys.forEach { key ->
			val modifierType = MageBlockModifierRegistry.MODIFIER_REGISTRY.get(Identifier(key))!!
			addModifier(modifierType.deserialize(modifiers.get(key)!!))
		}
	}
}