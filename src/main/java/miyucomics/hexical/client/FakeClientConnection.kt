package miyucomics.hexical.client

import net.minecraft.network.ClientConnection
import net.minecraft.network.NetworkSide

class FakeClientConnection(side: NetworkSide?) : ClientConnection(side)