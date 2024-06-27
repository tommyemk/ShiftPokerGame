package service

import entity.ShiftPokerGame
import view.Refreshable

/**
 * The root service that connects all other services and provides
 */
class RootService {
    val playerService = PlayerActionService(this)
    val pokerGameService = PokerGameService(this)

    var currentGame : ShiftPokerGame? = null



    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        pokerGameService.addRefreshable(newRefreshable)
        playerService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}