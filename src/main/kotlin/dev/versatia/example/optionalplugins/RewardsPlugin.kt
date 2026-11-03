package dev.versatia.example.optionalplugins

import dev.versatia.VersatiaPlugin

/** Works with or without Vault; `plugin.yml` lists it under `softdepend:` so that it enables first when present. */
class RewardsPlugin : VersatiaPlugin()
