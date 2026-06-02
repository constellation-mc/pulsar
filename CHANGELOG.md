# What's New:

- Port to Minecraft 26.1.2

This release contains a few renames to match the new mojang names, 
like `animateIcon` -> `extractAnimation`, `location` -> `identifier`.

`BrightLightTexture` was updated to extend `Lightmap` and needs to be used as a `GpuTextureView`.

`FakeLevel` might be a bit broken as this update stops loading of timelines.
