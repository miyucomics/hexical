from typing import Any

from hexdoc.core import ModResourceLoader
from hexdoc.model import ValidationContextModel

from .book.conjure_flora_recipe import ConjureFloraRecipe

class HexicalContext(ValidationContextModel):
    conjure_flora_recipes: list[ConjureFloraRecipe] = []

    def load_flora(self, loader: ModResourceLoader, context: dict[str, Any]):
        recipes = [
            ConjureFloraRecipe.load(resource_dir, flora_id, path, context)
            for resource_dir, flora_id, path in loader.load_resources("data", folder="recipes/flora", namespace="*")
        ]
        recipes.sort(key=lambda x: x.cost)
        self.conjure_flora_recipes = recipes
        return self
