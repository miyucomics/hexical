# Changelog

## 1.3.0
- added hexbursts
- added hextitos
- added wristpocket spell
- added Magic Missile spell
- added dye iota and patterns to get and change dyes
- added data-driven way to specify targets for dye spell
- added internal iota storage to hand lamp and patterns to interact with it
- added flecks: similar to specks, but they use a list of vectors to determine their shape
- added Similarity Distillation - get if two iota are of the same type
- added Congruence Distillation - get if two patterns are the same, regardless of orientation
- added Greater Blink spell: Blink with a relative positional and rotational offset for much cheaper than GTP
- added Charge, Retreat, Dodge, and Evade Reflections, which return how long you've been pressing the w, s, a, or d key respectively
- added mishap to Chorus Blink if you don't have chorus fruit in your inventory
- changed Identify pattern to have ambit limitation
- changed Make Genie spell to take a number representing media to draw from your inventory
- dropped support for Forge ( not that it worked on Forge to begin with )
- fixed advancements occasionally breaking
- fixed Conjure Speck spell costing too little
- fixed Janus' Gambit to actually terminate the hex now
- fixed specks being slightly off-center
- overhauled project structure completely
- removed casting sounds from conjured staves and replaced it with pattern drawing sounds
- updated documentation

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
- added mishap to grimoire patterns if you are not holding a grimoire in your offhand
- added `zh_cn` translation
- added identifier iota
- made conjure staff take in the number of dust for battery
- fixed creative inventory lamps
- fixed telepathy on servers

## 1.0.0
- Initial release, let's go!