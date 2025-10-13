from importlib.resources import Package
from typing import Any

import hexdoc_hexical
from hexdoc.core.loader import ModResourceLoader
from hexdoc.plugin import (HookReturn, ModPlugin, ModPluginImpl, ModPluginWithBook, hookimpl)
from hexdoc.utils import ValidationContext
from typing_extensions import override

from .__gradle_version__ import FULL_VERSION, GRADLE_VERSION
from .__version__ import PY_VERSION
from .book import conjure_flora_recipe, transmuting_recipe
from .book.page import pages
from .metadata import HexicalContext

class HexicalPlugin(ModPluginImpl):
    @staticmethod
    @hookimpl
    def hexdoc_mod_plugin(branch: str) -> ModPlugin:
        return HexicalModPlugin(branch=branch)

    @staticmethod
    @hookimpl
    def hexdoc_load_tagged_unions() -> HookReturn[Package]:
        return [conjure_flora_recipe, transmuting_recipe, pages]

    @staticmethod
    @hookimpl
    def hexdoc_update_context(context: dict[str, Any]) -> HookReturn[ValidationContext]:
        loader = ModResourceLoader.of(context)
        return HexicalContext().load_flora(loader, context)

class HexicalModPlugin(ModPluginWithBook):
    @property
    @override
    def modid(self) -> str:
        return "hexical"

    @property
    @override
    def full_version(self) -> str:
        return FULL_VERSION

    @property
    @override
    def mod_version(self) -> str:
        return GRADLE_VERSION

    @property
    @override
    def plugin_version(self) -> str:
        return PY_VERSION

    @override
    def resource_dirs(self) -> HookReturn[Package]:
        # lazy import because generated may not exist when this file is loaded
        # eg. when generating the contents of generated
        # so we only want to import it if we actually need it
        from ._export import generated

        return generated

    @override
    def jinja_template_root(self) -> tuple[Package, str]:
        return hexdoc_hexical, "_templates"
