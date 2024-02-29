package miyucomics.hexical.mixin;

import miyucomics.hexical.registry.HexicalItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {
	@Shadow protected native void fillRecipes();
	@Shadow protected native void afterUsing(TradeOffer offer);

	public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "fillRecipes", at = @At("RETURN"))
	public void addNewTrades(CallbackInfo info) {
		TradeOfferList tradeOfferList = getOffers();
		if (tradeOfferList == null)
			return;
		if (random.nextFloat() < 0.1f)
			tradeOfferList.add(new TradeOffer(new ItemStack(Items.EMERALD, 32), new ItemStack(HexicalItems.INSTANCE.getLAMP_ITEM(), 1), 1, 1, 1));
	}
}