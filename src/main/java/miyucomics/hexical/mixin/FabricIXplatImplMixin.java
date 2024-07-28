package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.fabric.xplat.FabricXplatImpl;
import miyucomics.hexical.entities.LivingScrollEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FabricXplatImpl.class)
public class FabricIXplatImplMixin {
	@Inject(method = "findDataHolder(Lnet/minecraft/entity/Entity;)Lat/petrak/hexcasting/api/addldata/ADIotaHolder;", at = @At("HEAD"), cancellable = true)
	private void readFromLivingScrolls(Entity entity, CallbackInfoReturnable<ADIotaHolder> cir) {
		if (entity instanceof LivingScrollEntity)
			cir.setReturnValue((LivingScrollEntity) entity);
	}
}