package service

import entity.Card
import entity.CardSuit
import entity.CardValue

/**
 * A service class responsible for managing player actions such as shifting and swapping cards during the game
 *
 * @property rootService A reference to the RootService, used to access and manage the current game state
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Shifts the middle cards to the left or right and updates the game state accordingly
     *
     * @param left A boolean indicating the direction of the shift. True for left, false for right
     * @throws IllegalStateException if the current player has already shifted cards this turn
     */
    fun shift(left: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "game must be initialized" }

        // Ensures the current player hasn't already shifted cards this turn.
        if (game.players[game.currentPlayer].hasShifted) throw IllegalStateException()

        val middleCards = game.board.middleCards
        val board = game.board

        if (left) {
            // Shifts the middle cards to the left and replaces the free spot with a card from the draw pile.
            board.leftdiscardPile.add(Card(CardSuit.CLUBS,CardValue.ACE))
            board.leftdiscardPile[0] = middleCards[0]
            middleCards[0] = middleCards[1]
            middleCards[1] = middleCards[2]
            middleCards[2] = board.drawPile.removeLast()
        } else {
            // Shifts the middle cards to the right and replaces the free spot with a card from the draw pile.
            board.rightDiscardPile.add(Card(CardSuit.CLUBS, CardValue.ACE))
            board.rightDiscardPile[0] = middleCards[2]
            middleCards[2] = middleCards[1]
            middleCards[1] = middleCards[0]
            middleCards[0] = board.drawPile.removeLast()
        }
        // Marks that the current player has completed their shift action.
        game.players[game.currentPlayer].hasShifted = true

        // Refreshes the GUI to reflect the changes.
        onAllRefreshables { refreshAfterShiftCard(left) }

    }

    /**
     * Swaps all the current player's open cards with the middle cards
     * @throws IllegalStateException if the current player hasn't shifted cards this turn
     */
    fun swapAll() {
        val game = rootService.currentGame
        checkNotNull(game) { "game must be initialized" }

        val currentPlayer = game.players[game.currentPlayer]

        if (!currentPlayer.hasShifted) throw IllegalStateException()

        // Swaps the player's open cards with the middle cards.
        val temp = currentPlayer.openCards
        currentPlayer.openCards = game.board.middleCards
        game.board.middleCards = temp

        // Refreshes the GUI and moves to the next player.
        onAllRefreshables { refreshAfterSwapCard(all = true) }
        rootService.pokerGameService.nextPlayer()
        onAllRefreshables { refreshAfterNextPlayer() }
    }

    /**
     * Allows the current player to pass their turn without swapping cards
     * @throws IllegalStateException if the current player hasn't shifted cards this turn
     */
    fun pass() {
        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer = game.players[game.currentPlayer]

        if (!currentPlayer.hasShifted) throw IllegalStateException()

        // Moves to the next player.
        rootService.pokerGameService.nextPlayer()
        // Refreshes the GUI.
        onAllRefreshables { refreshAfterNextPlayer() }
    }

    /**
     * Swaps a single open card of the current player with one of the middle cards
     *
     * @param handIndex The index of the player's open card to swap
     * @param middleIndex The index of the middle card to swap with
     * @throws IllegalStateException if the current player hasn't shifted cards this turn
     * @throws IndexOutOfBoundsException if either index is out of the allowable range (0..2)
     */
    fun swap(handIndex: Int, middleIndex: Int) {
        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer = game.players[game.currentPlayer]

        if (!currentPlayer.hasShifted) throw IllegalStateException()

        if (handIndex !in 0..2 || middleIndex !in 0..2) throw IndexOutOfBoundsException(
            "handIndex and middleIndex must be in the range 0..2"
        )

        // Swaps the specified cards.
        val temp = currentPlayer.openCards[handIndex]
        currentPlayer.openCards[handIndex] = game.board.middleCards[middleIndex]
        game.board.middleCards[middleIndex] = temp

        // Refreshes the GUI and moves to the next player.
        onAllRefreshables { refreshAfterSwapCard(all = false) }
        println("reached")
        rootService.pokerGameService.nextPlayer()
        println(game.players[game.currentPlayer].hasShifted)
        onAllRefreshables { refreshAfterNextPlayer() }
    }
}
