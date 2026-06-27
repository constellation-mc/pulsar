# What's New:

**This update breaks everything that depends on Pulsar!!**

The biggest change in this update is the return of `api`/`impl` packages, 
which was something that should've been there from the beginning, but oh well.

- Abstracted away most of Fabric in the API.
- Creative tab animations now live in the `client` package, fixed the spelling mistake.
- Moved events to a custom simple event bus.
- Renamed 'Reloaders' to 'ReloadListeners'.
- Removed Fabric-specific register method in `ServerReloadListenersEvent`.
- Rewrote screen particles to be tied to the `Minecraft` object.
- Moved most of `fakelevel` into the `impl` package.
- Disabled particle methods in `fakelevel`
