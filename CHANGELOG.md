# What's New:

- Port to Minecraft 1.21.10

## ExtraCodecs

This port removes `fromJsonSerializer` and other `JsonSerializer` related methods, 
as `JsonSerializer` is no longer a thing.

## ReloaderType

Removed `TagManager` and `LootDataManager` types, as those were moved to 
dynamic registries.

## SavedData

Removed `SavedDataHelper`, as SavedData was refactored into using datafixerupper codecs,
making this class obsolete.

Additionally, Fabric provides a data attachment API, which is a (better) replacement for those classes.
