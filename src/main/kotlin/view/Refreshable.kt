package view
import entity.*

/**
 * Interface for classes that can be refreshed after a game action.

 */
interface Refreshable {
    /**
     * Refreshes the view after a card has been shifted.
     * @param left true if the card was shifted to the left, false if it was shifted to the right
     */
    fun refreshAfterShiftCard(left:Boolean){}
    /**
     * Refreshes the view after a card has been swapped.
     * @param all true if all cards were swapped, false if only one card was swapped
     */
    fun refreshAfterSwapCard(all : Boolean){}
    /**
     * Refreshes the view after the game has started.
     */
    fun refreshAfterStartGame(){}
    /**
     * Refreshes the view after the next player has been selected.
     */
    fun refreshAfterNextPlayer(){}
    /**
     * Refreshes the view after the game has ended.
     * @param result the result of the game
     */
    fun refreshAfterGameEnd(result : Map<Player, Int>){}
}