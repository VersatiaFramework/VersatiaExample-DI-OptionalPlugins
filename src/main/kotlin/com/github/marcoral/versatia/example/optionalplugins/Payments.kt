package com.github.marcoral.versatia.example.optionalplugins

import com.github.marcoral.versatia.di.LocalBean
import org.bukkit.OfflinePlayer
import java.util.UUID

/** What the plugin needs from an economy; the beans below provide it with Vault or without. */
interface Payments {
    val description: String

    fun deposit(player: OfflinePlayer, amount: Double)

    fun balance(player: OfflinePlayer): String
}

/** The fallback, always a candidate: without Vault the `@Primary` provider does not exist and this one is the only one left. */
@LocalBean
class MemoryPayments : Payments {
    private val balances = mutableMapOf<UUID, Double>()

    override val description = "balances kept in memory until the server stops"

    override fun deposit(player: OfflinePlayer, amount: Double) {
        balances.merge(player.uniqueId, amount, Double::plus)
    }

    override fun balance(player: OfflinePlayer) = "${balances[player.uniqueId] ?: 0.0} coins"
}
