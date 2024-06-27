package entity
/**
 * Represents the game board for a card game, organizing the different piles of cards used during play.
 *
 * This class manages the state of the game board, including the discard piles on both the left and right sides,
 * the middle cards visible to all players, and the draw pile from which players draw new cards.
 */
class Board {
     var leftdiscardPile: MutableList<Card> = mutableListOf()
     var rightDiscardPile: MutableList<Card> = mutableListOf()
     var middleCards: MutableList<Card> = mutableListOf()
     var drawPile: MutableList<Card> = mutableListOf()
}
