package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import miyucomics.hexical.interfaces.CastingContextMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CastingContext.class)
public abstract class CastingContextMixin implements CastingContextMixinInterface {
	@Unique private boolean hexical$isCastByLamp = false;
	@Unique private boolean hexical$archlamp = false;
	@Override public boolean getCastByLamp() {return hexical$isCastByLamp;}
	@Override public void setCastByLamp(boolean value) {hexical$isCastByLamp = value;}
	@Override public boolean getArchLamp() {return hexical$archlamp;}
	@Override public void setArchLamp(boolean value) {hexical$archlamp = value;}
}