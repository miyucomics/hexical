package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.nbt.NbtElement
import net.minecraft.util.Identifier

abstract class MageBlockModifierType<T : MageBlockModifier> {
	abstract val argc: Int
	abstract val id: Identifier
	abstract fun construct(args: List<Iota>): T
	abstract fun deserialize(element: NbtElement): T
}