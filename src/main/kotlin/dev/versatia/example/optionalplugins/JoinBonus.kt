package dev.versatia.example.optionalplugins

import dev.versatia.Phase
import dev.versatia.di.LocalBean
import dev.versatia.lifecycle.AutoInvoke
import dev.versatia.text.Text
import dev.versatia.text.arg
import java.util.logging.Logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/** Takes `Payments` without knowing which one it gets: `VaultPayments` with Vault, `MemoryPayments` without. */
@LocalBean
class JoinBonus(private val payments: Payments, private val logger: Logger, private val text: Text) : Listener {
    init {
        logger.info("JoinBonus pays through ${payments.javaClass.simpleName}: ${payments.description}")
    }

    /** Through `@NeedsVault`: called only with Vault. The bean exists either way, only the call is skipped. */
    @NeedsVault
    @AutoInvoke(Phase.PLUGIN_START_ONLY)
    fun vaultFound() = logger.info("Vault found, join bonuses go to players' Vault accounts")

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        payments.deposit(event.player, BONUS)
        event.player.sendMessage(text.parse("<positive>Join bonus: {bonus}, balance {balance}", arg("bonus", BONUS), arg("balance", payments.balance(event.player))))
        logger.info("${event.player.name} received the join bonus, balance ${payments.balance(event.player)}")
    }

    private companion object {
        const val BONUS = 10.0
    }
}
