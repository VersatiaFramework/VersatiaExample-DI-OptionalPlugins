package com.github.marcoral.versatia.example.optionalplugins

import com.github.marcoral.versatia.Phase
import com.github.marcoral.versatia.di.LocalBean
import com.github.marcoral.versatia.lifecycle.AutoInvoke
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/** Takes `Payments` without knowing which one it gets: `VaultPayments` with Vault, `MemoryPayments` without. */
@LocalBean
class JoinBonus(private val payments: Payments, private val plugin: RewardsPlugin) : Listener {
    init {
        plugin.logger.info("JoinBonus pays through ${payments.javaClass.simpleName}: ${payments.description}")
    }

    /** Through `@NeedsVault`: called only with Vault. The bean exists either way, only the call is skipped. */
    @NeedsVault
    @AutoInvoke(Phase.PLUGIN_START_ONLY)
    fun vaultFound() = plugin.logger.info("Vault found, join bonuses go to players' Vault accounts")

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        payments.deposit(event.player, BONUS)
        event.player.sendMessage(Component.text("Join bonus: $BONUS, balance ${payments.balance(event.player)}"))
        plugin.logger.info("${event.player.name} received the join bonus, balance ${payments.balance(event.player)}")
    }

    private companion object {
        const val BONUS = 10.0
    }
}
