package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlock

class ConjuredSlipperyBlock : GenericConjuredBlock<ConjuredSlipperyBlockEntity>(baseMaterial().slipperiness(1f), ConjuredSlipperyBlockEntity::init)