package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import miyucomics.hexical.interfaces.CastingContextMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CastingContext.class)
public abstract class CastingContextMixin implements CastingContextMixinInterface {
	@Unique private boolean hexical$isCastByLamp;

	@Override public boolean getCastByLamp() {
		return hexical$isCastByLamp;
	}

	@Override public void setCastByLamp(boolean value) {
		hexical$isCastByLamp = value;
	}
}