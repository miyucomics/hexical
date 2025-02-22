package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.OpErase$Spell", remap = false)
public class OpErase$SpellMixin {
	@Inject(method = "cast(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)V", at = @At("HEAD"))
	private void eraseAutographs(CastingEnvironment env, CallbackInfo ci) {
		(((OpErase$SpellAccessor) this).getStack()).getOrCreateNbt().remove("autographs");
	}
}