package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.utils.putCompound
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
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

	fun addScryingLensLines(lines: MutableList<Pair<ItemStack, Text>>) {
		modifiers.forEach {
			val line = it.value.getScryingLens()
			if (line != null)
				lines.add(line)
		}
	}

	override fun saveModData(compound: NbtCompound) {
		compound.putCompound("modifiers", NbtCompound().apply {
			modifiers.forEach {
				put(it.key.toString(), it.value.serialize())
			}
		})
	}

	override fun loadModData(compound: NbtCompound) {
		val serializedModifiers = compound.getCompound("modifiers")
		serializedModifiers.keys.forEach { key ->
			val modifierType = MageBlockModifierRegistry.MODIFIER_REGISTRY.get(Identifier(key))!!
			modifiers[modifierType.id] = modifierType.deserialize(serializedModifiers.get(key)!!)
		}
	}
}