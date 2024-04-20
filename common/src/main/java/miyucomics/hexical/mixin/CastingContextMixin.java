package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import miyucomics.hexical.interfaces.CastingContextMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CastingContext.class)
public abstract class CastingContextMixin implements CastingContextMixinInterface {
	@Unique
	private boolean hexical$isCastByLamp = false;
	@Unique
	private boolean hexical$archLamp = false;
	@Unique
	private boolean hexical$finale = false;

	@Override
	public boolean hexical$getCastByLamp() {
		return hexical$isCastByLamp;
	}

	@Override
	public boolean hexical$getArchLamp() {
		return hexical$archLamp;
	}

	@Override
	public boolean hexical$getFinale() {
		return hexical$finale;
	}

	@Override
	public void hexical$setCastByLamp(boolean value) {
		hexical$isCastByLamp = value;
	}

	@Override
	public void hexical$setArchLamp(boolean value) {
		hexical$archLamp = value;
	}

	@Override
	public void hexical$setFinale(boolean value) {
		hexical$finale = value;
	}
}