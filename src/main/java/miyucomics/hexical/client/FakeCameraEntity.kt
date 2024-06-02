package miyucomics.hexical.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.recipebook.ClientRecipeBook
import net.minecraft.client.world.ClientWorld
import net.minecraft.stat.StatHandler

class FakeCameraEntity(client: MinecraftClient, world: ClientWorld): ClientPlayerEntity(client, world, FakeClientPlayNetworkHandler(client), StatHandler(), ClientRecipeBook(), false, false) {

}