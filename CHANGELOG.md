# What's New:

- added a `ServerReloadersEvent#reload` overload that accepts a location and a regular listener.
internally this wraps the listener in a `IdentifiableResourceReloadListener`, to prepare for IRRLs deprecation in future versions.
- `PulsarEntries#appendStacks` now defaults to tab & search,
added a method to override this.