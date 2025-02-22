package miyucomics.hexical.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.OpErase$Spell")
public interface OpErase$SpellAccessor {
	@Accessor("stack")
	ItemStack getStack();
}