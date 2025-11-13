package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.addldata.ItemDelegatingEntityIotaHolder
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.getList
import net.minecraft.nbt.NbtElement

class AnimatedScrollReader(scrollEntity: AnimatedScrollEntity) : ItemDelegatingEntityIotaHolder({ scrollEntity.scroll.copy() }, { stack ->
	scrollEntity.scroll = stack
	scrollEntity.patterns = stack.getList("patterns", NbtElement.COMPOUND_TYPE.toInt())!!.map { it.asCompound }
	scrollEntity.updateRender()
})