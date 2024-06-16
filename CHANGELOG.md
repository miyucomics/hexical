# Changelog

## 1.3.0
- added hexbursts - add iota to your stack when eaten
- added hextitos - cast spell when eaten, using player stack
- added wristpocket spell - hide items magically
    - added Ingest spell - eat wristpocketed item
    - added Mage Hand spell - use your wristpocketed item to interact with blocks and entities
- added dye iota
    - added Chromatic Purification and Dye spell to get and set dyes
    - added data-pack based method to add custom color associations to blocks so you can add support for mods with simple datapacks
- added evocation - a keybind that allows you to cast spells by holding a keybind for some time
- added Scry Sentinel - see through your sentinel!
- added Command Golem - command player-made golems to attack a target
- added Similarity Distillation - get if two iota are of the same type
- added Congruence Distillation - get if two patterns are the same, regardless of orientation
- added internal iota storage to hand lamp and patterns to interact with it
- added Magic Missile spell - fire silver of amethyst that deals knockback and weak damage
- added flecks - similar to specks, but they use a list of vectors to determine their shape
- added Greater Blink spell: Blink with a relative positional and rotational offset for much cheaper than GTP
- added Charge, Retreat, Dodge, and Evade Reflections - return how long you've pressed the w, s, a, or d key
- added Prestidigitation spell, which has dozens of small magical effect. Think Sonic Screwdriver, but magic
    - Prestidigitation is also data-driven, allowing you to add simple effects to any block.
    - exposed registry for prestidigitation effects, allowing you to make an addon that adds your own magical effects.
- added mishap to Chorus Blink if you don't have chorus fruit in your inventory
- changed Identify pattern to have ambit limitation
- changed Make Genie spell to take a number representing media to draw from your inventory
    - this is a massive buff to the Make Genie spell since you can now give virtually endless media to it
- dropped support for Forge in order to prioritize development speed and features
- fixed specks being slightly off-center
- fixed advancements occasionally breaking
- fixed Conjure Speck spell costing too little
- fixed Janus' Gambit to actually terminate the hex now
- fixed bug with Archgenie Lamp where it can cast for free if it wasn't in your main inventory
- overhauled project structure completely
- removed casting sounds from conjured staves and replaced it with pattern drawing sounds
- updated documentation
- updated telepathy code to share code with movement reflections

## 1.2.0
- added more achievements
- added lightning rod staff, with strong knockback and slow swing speed
- added Displace spell for circles to teleport entities for cheap
- added energized mage blocks modifier to emit Redstone power
- added living scrolls
- added proper speck text rendering
- added more meta-evals
    - added Dioscuri's Gambit
    - added Janus' Gambit
    - added Sisyphus' Gambit
- added z-axis rotation for specks
- added iota storage for conjured staves
- added Dioscuri Gambit II
- changed Conjure Speck to push the speck to the stack
- changed Nephthys' Gambit to no longer need a number, instead relying on tail length
- fixed accidental swapping of Sloth and Racer's Purification
- fixed being able to use Recharge Item to recharge lamps
- fixed conjured staves not casting properly
- fixed mage block breaking particles
- fixed Nephthys' Gambit not working on single patterns
- fixed speck pattern saving
- remove ambit requirement for altering specks
- updated documentation
- overhauled a lot of code
- overhauled world scrying
    - added enchantment patterns
    - added entity patterns
    - added food patterns
    - added identifier patterns
    - added item patterns
    - added status effect patterns
    - added world patterns

## 1.1.0
- added identifier iota
- added `zh_cn` translation
- added mishap to grimoire patterns if you are not holding a grimoire in your offhand
- changed creative inventory lamps to have media by default
- changed Conjure Staff to take in dust rather than media for battery
- fixed telepathy crashing on servers

## 1.0.0
- Initial release, let's go!