from json import loads

from hexdoc.core import ModResourceLoader, ResourceLocation
from hexdoc.model import ValidationContextModel
from pydantic import Field

from .book.conjure_flora_recipe import ConjureFloraRecipe

class HexicalContext(ValidationContextModel):
    conjure_flora_recipes: dict[ResourceLocation, ConjureFloraRecipe] = Field(default_factory=dict)

    def load_flora(self, loader: ModResourceLoader):
        for resource_dir, flora_id, path in loader.find_resources("data", folder="recipes", namespace="*", glob="**/*.json", internal_only=True, allow_missing=True):
            recipe = loads(path.read_text("utf-8"))
            if recipe["type"] != "hexical:conjure_flora":
                continue

            # do something with recipe??
