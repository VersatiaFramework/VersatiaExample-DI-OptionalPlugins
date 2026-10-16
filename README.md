# VersatiaExample-DI-OptionalPlugins

An integration with an optional plugin: a bean conditional on Vault with `@ConditionalOnPlugin` and `@Primary`, a fallback used when Vault is missing, and the condition written once as an annotation of your own.

Part of the [Versatia examples](https://github.com/VersatiaFramework/VersatiaExamples); documentation: [Optional plugins](https://versatiaframework.github.io/VersatiaDocs/docs/optional-plugins).

**Start with** [`Payments.kt`](src/main/kotlin/com/github/marcoral/versatia/example/optionalplugins/Payments.kt) and [`VaultPayments.kt`](src/main/kotlin/com/github/marcoral/versatia/example/optionalplugins/VaultPayments.kt), then [`JoinBonus.kt`](src/main/kotlin/com/github/marcoral/versatia/example/optionalplugins/JoinBonus.kt), which uses them, and [`NeedsVault.kt`](src/main/kotlin/com/github/marcoral/versatia/example/optionalplugins/NeedsVault.kt).

## What the log shows

Without Vault:

```
[VersatiaExample-DI-OptionalPlugins] Skipped VaultPayments, JoinBonus.vaultFound(): Vault is not installed
[VersatiaExample-DI-OptionalPlugins] JoinBonus pays through MemoryPayments: balances kept in memory until the server stops
```

`VaultPayments` is not created and its class never loaded; of `JoinBonus` only the `@NeedsVault`
function is skipped, while the bean itself is created. With Vault and an economy plugin
(EssentialsX, say):

```
[VersatiaExample-DI-OptionalPlugins] JoinBonus pays through VaultPayments: Vault, economy <your economy plugin>
[VersatiaExample-DI-OptionalPlugins] Vault found, join bonuses go to players' Vault accounts
```

When a player joins, either way:

```
[VersatiaExample-DI-OptionalPlugins] Steve received the join bonus, balance 10.0 coins
```

## Things to try

- Start the server without Vault, join, then add Vault and an economy plugin and restart: the
  bonus now lands in the Vault account, and nothing in `JoinBonus` changed.
- Remove `@Primary` from `VaultPayments` and build: with Vault both beans provide `Payments`, so
  the build fails with `Dependency ...Payments of ...JoinBonus is ambiguous while "Vault" is enabled; candidates: ...`.
- Remove `MemoryPayments` and build: `...JoinBonus depends on ...VaultPayments, which exists only
  with @ConditionalOnPlugin("Vault"); ...`. The build checks every combination of the optional plugins.
- Replace `@ConditionalOnPlugin("Vault")` on `VaultPayments` with `@NeedsVault`: the descriptor in
  `build/generated/ksp/main/kotlin` records the same condition, inherited from the annotation.
- Remove `softdepend:` from `plugin.yml` and start with Vault: the log may read
  `Skipped ...: Vault is installed but was not enabled before VersatiaExample-DI-OptionalPlugins; list it under softdepend: in plugin.yml`.
- Install Vault without an economy plugin: Vault itself provides no economy, so `VaultPayments`
  fails to create with `No provider of net.milkbowl.vault.economy.Economy is registered. ...`.

## Building

Requires JDK 21 and VersatiaStarter with what it exports (VersatiaAPI, the module APIs) in your
local Maven repository (`./gradlew publishToMavenLocal` in each checkout; see
[Building the framework](https://versatiaframework.github.io/VersatiaDocs/docs/contributing/building));
running it requires a Paper 1.21.4 server with VersatiaCore installed, and, to see the Vault side,
[Vault](https://github.com/MilkBowl/Vault) with an economy plugin. The build compiles against
VaultAPI from [JitPack](https://jitpack.io) (`compileOnly`, never bundled).

```bash
./gradlew build
```

To have the JAR copied to your server after every build, copy `local.properties.example` to
`local.properties` and set `pluginsDir`. The generated descriptor is in
`build/generated/ksp/main/kotlin`; open it to see exactly what the runtime executes.
