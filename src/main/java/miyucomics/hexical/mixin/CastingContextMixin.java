package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import miyucomics.hexical.enums.SpecializedSource;
import miyucomics.hexical.interfaces.CastingContextMinterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CastingContext.class)
public class CastingContextMixin implements CastingContextMinterface {
	@Unique
	private boolean hexical$finale = false;
	@Unique
	private SpecializedSource hexical$castSource = null;
	@Unique
	private int hexical$soroban = 0;

	@Override
	public boolean getFinale() {
		return hexical$finale;
	}

	@Override
	public void setFinale(boolean value) {
		hexical$finale = value;
	}

	@Override
	public void setSpecializedSource(@NotNull SpecializedSource source) {
		hexical$castSource = source;
	}

	@Nullable
	@Override
	public SpecializedSource getSpecializedSource() {
		return hexical$castSource;
	}

	@Override
	public int getSoroban() {
		return hexical$soroban;
	}

	@Override
	public void incrementSoroban() {
		hexical$soroban += 1;
	}

	@Override
	public void decrementSoroban() {
		hexical$soroban -= 1;
	}

	@Override
	public void resetSoroban() {
		hexical$soroban = 0;
	}
}