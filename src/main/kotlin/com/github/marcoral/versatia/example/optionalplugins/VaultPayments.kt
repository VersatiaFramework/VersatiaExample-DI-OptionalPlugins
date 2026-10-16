package com.github.marcoral.versatia.example.optionalplugins

import com.github.marcoral.versatia.ConditionalOnPlugin
import com.github.marcoral.versatia.VersatiaEngine
import com.github.marcoral.versatia.di.LocalBean
import com.github.marcoral.versatia.di.Primary
import com.github.marcoral.versatia.loadService
import net.milkbowl.vault.economy.Economy
import org.bukkit.OfflinePlayer

/**
 * Exists only while Vault is enabled, and then `@Primary` makes it the `Payments` everyone gets. Without Vault the
 * class is never loaded, so it uses Vault's classes freely.
 */
@ConditionalOnPlugin("Vault")
@LocalBean
@Primary
class VaultPayments : Payments {
    private val economy = VersatiaEngine.loadService<Economy>()

    override val description: String get() = "Vault, economy ${economy.name}"

    override fun deposit(player: OfflinePlayer, amount: Double) {
        economy.depositPlayer(player, amount)
    }

    override fun balance(player: OfflinePlayer): String = economy.format(economy.getBalance(player))
}
