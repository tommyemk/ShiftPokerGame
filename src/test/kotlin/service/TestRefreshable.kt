package service

import entity.Player
import view.Refreshable

/**
 * A test implementation of [Refreshable] used to test the functionality of the [PokerGameService].
 */
class TestRefreshable : Refreshable {
    var refreshAfterShiftCard = false
        private set

    var refreshAfterSwapCard = false
        private set

    var refreshAfterStartGame = false
        private set

    var refreshAfterNextPlayer = false
        private set

    var refreshAfterGameEnd = false
        private set

    fun reset() {
        refreshAfterShiftCard = false
        refreshAfterSwapCard = false
        refreshAfterStartGame = false
        refreshAfterNextPlayer = false
        refreshAfterGameEnd = false
    }

    override fun refreshAfterGameEnd(result: Map<Player, Int>) {
        refreshAfterGameEnd = true
    }

    override fun refreshAfterNextPlayer() {
        refreshAfterNextPlayer = true
    }

    override fun refreshAfterShiftCard(left: Boolean) {
        refreshAfterShiftCard = true
    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGame = true
    }

    override fun refreshAfterSwapCard(all: Boolean) {
        refreshAfterSwapCard = true
    }
}