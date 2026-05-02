# Pulsar docs

Pulsar is a library mod, continuation of [dark-matter](https://github.com/constellation-mc/dark-matter). 
unlike dark-matter, pulsar is a monolithic util mod, which users download separately.
it also opts for mojang mappings over yarn.

these docs are accurate for the Minecraft 1.20.1 version of the mod.

this is a quick overview of the library features, as each class provides javadoc.

out of the more interesting utils, pulsar provides:

## GUI Particles

package: `client.particles`

this util enables drawing particle-like things on top of the player's screen. 
it comes with an extension to even draw vanilla particles.

## Extended Creative Mode Tab

package: `creativetab`

provides a way to add multiple of the same item to the group. (e.g. air for grouping)

on the client provides a way to add a custom tab icon renderer.

## Mixin Util

package: `mixin`

the `VirtualMixins` class provides a way to add "virtual" mixin configs at runtime.
mainly useful for modular mods like Andromeda.

## Reload Listeners

package: `resources`

provides an event to register reload listeners with `registryAccess` & `featureFlags` contexts.