package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.utils.putCompound
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class MageBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.MAGE_BLOCK_ENTITY, pos, state) {
	val modifiers: MutableMap<Identifier, MageBlockModifier> = mutableMapOf()
	var disguise: BlockState = Blocks.AMETHYST_BLOCK.defaultState

	fun addModifier(modifier: MageBlockModifier) {
		modifiers[modifier.type.id] = modifier
		this.sync()
	}

	fun clearModifiers() {
		modifiers.clear()
		this.sync()
	}

	fun setBlockState(state: BlockState) {
		this.disguise = state
		val currentState = this.cachedState
		val newState = currentState.with(MageBlock.UPDATE_TRIGGER, !currentState.get(MageBlock.UPDATE_TRIGGER))
		this.world!!.setBlockState(this.pos, newState, 3)
		this.sync()
	}

	fun sync() {
		this.markDirty()
		this.world!!.updateNeighborsAlways(pos, HexicalBlocks.MAGE_BLOCK)
		this.world!!.updateListeners(this.getPos(), this.cachedState, this.cachedState, 3)
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)

	fun <T : MageBlockModifier> getModifier(type: MageBlockModifierType<T>): T = modifiers[type.id] as T
	fun hasModifier(type: MageBlockModifierType<*>) = modifiers.containsKey(type.id)

	fun addScryingLensLines(lines: MutableList<Pair<ItemStack, Text>>) {
		modifiers.forEach {
			val line = it.value.getScryingLens()
			if (line != null)
				lines.add(line)
		}
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putCompound("disguise", NbtHelper.fromBlockState(disguise))
		compound.putCompound("modifiers", NbtCompound().apply {
			modifiers.forEach {
				put(it.key.toString(), it.value.serialize())
			}
		})
	}

	override fun readNbt(compound: NbtCompound) {
		this.disguise = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), compound.getCompound("disguise"))
		val serializedModifiers = compound.getCompound("modifiers")
		serializedModifiers.keys.forEach { key ->
			val modifierType = MageBlockModifierRegistry.MODIFIER_REGISTRY.get(Identifier(key))!!
			modifiers[modifierType.id] = modifierType.deserialize(serializedModifiers.get(key)!!)
		}
	}
}