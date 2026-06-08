# Contributing to Pulsar

## Formatting

Pulsar uses spotless to apply formatting, 
`spotlessCheck` gradle task must not fail, issues may be fixed via the `spotlessApply` task.

## Tests and Testmod

Any new bugfixes/methods should be added to the pulsar test/testmod.

JUnit tests, which usually don't require a server/level/client can be added 
to `src/test`. the `test` gradle task must pass.

For more complex tests, there is gametest and autotest, in the `dev.zenfyr.pulsar.test.autotest` package.

Tests that require initialization are implemented as normal entrypoints, and are added
to `fabric.mod.json` in the `preLaunch`/`main`/`client`,`server` blocks.

### Gametest

This is minecraft's built-in gametest,
Entrypoints in `fabric-gametest` will be executed with a headless server and a structure.
All late checks from `Util.addLateCheck` will still apply here.

The `runGametest` gradle task must pass.

### Autotest

Unlike gametest, autotest runs a full client/server with a few tweaks and utils to aid automation.

Tests are implemented as `ClientTestEntrypoint` & `ServerTestEntrypoint` 
and are added to `pulsar:client_test`/`pulsar:server_test` blocks in `fabric.mod.json`.

The TestEntrypoint classes work like minecraft's gametest, 
where object methods annotated as `@AutoTest` will be executed and report their success/fail state.

Both `runAutotest` and `runAutotestClient` gradle tasks must pass successfully.
