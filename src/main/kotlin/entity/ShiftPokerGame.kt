package entity
/**
 * Represents a game of Shift Poker, including all the necessary components and state to conduct a game round.
 *
 * This class encapsulates the state of a Shift Poker game, including the number of rounds to be played,
 * the current player's index, the game board, and the list of players participating in the game.
 *
 * @property rounds The total number of rounds the game will consist of. This value is set at the beginning of the game.
 * @property currentPlayer An integer representing the index of the current player in the players list. This is used to
 *              track whose turn it is to play.
 * @property board An instance of the Board class, representing the game board state, including the draw pile,
 *              discard piles, and middle cards.
 */
class ShiftPokerGame(var rounds : Int, val board : Board) {
    var players: List<Player> = mutableListOf()
    var currentPlayer : Int = 0
}
