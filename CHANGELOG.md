# What's New:

- Added `loot.LootCodecs` & `loot.LootContextBuilder` from Andromeda and Commander. 
    - in 1.20.1, `LootCodecs` is a wrapper around `Json(De)SerializationContext`, in future versions it uses the real codecs.
    - `LootContextBuilder` is a wrapper around `LootParams.Builder` with some utility methods added.
- Exposed custom dynamic ops in `JsonCodecDataLoader` and finalized the `apply` to match pulsar 1.20.1