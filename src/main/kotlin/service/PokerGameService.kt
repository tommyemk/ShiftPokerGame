package service

import entity.*

/**
 * A service class that manages the game logic for a poker game, including starting and ending a game,
 * and managing the game state
 *
 * @property rootService A reference to the RootService, used to access and manipulate the game state
 */
class PokerGameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Starts a new game of poker with the specified players and number of rounds
     *
     * @param players A list of player names
     * @param rounds The number of rounds to play
     * @throws IllegalArgumentException if the number of players is not between 2 and 4 or rounds are not between 2 and 7
     */
    fun startGame(players: List<String>, rounds: Int) {

        // create a new Game instance and safe it in currentGame
        val newGame = ShiftPokerGame(rounds, Board())
        rootService.currentGame = newGame
        val game = rootService.currentGame
        checkNotNull(game)

        // Validates the number of players and rounds.
        if (players.size !in 2..4 || rounds !in 2..7) {
            throw IllegalArgumentException()
        }

        // Sets the game rounds and initializes players
        game.rounds = rounds
        game.players = players.map { Player(it, false) }.shuffled()

        val cardDeck: MutableList<Card> = createFullDeck().shuffled().toMutableList()

        // Distributes cards to players' open and hidden hands.
        for (player in game.players) {
            repeat(3) {
                player.openCards.add(cardDeck.removeFirst())
            }

            repeat(2) {
                player.hiddenCards.add(cardDeck.removeFirst())

            }
        }

        for (player in game.players) {
            println(player.openCards.size)
        }

        // Initializes the middle cards on the board.

        repeat(3) {
            game.board.middleCards.add(cardDeck.removeFirst())
        }

        // Remaining cards form the draw pile.
        game.board.drawPile = cardDeck

        // Refreshes the game state for all observers.
        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     * Ends the current game.
     * This method concludes the current game session by calculating the results for each player
     * and then refreshing the UI or game state to reflect the game's end.
     */
     fun endGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        val results = calcResults() // Calculate the results for each player.
        println(results)
        onAllRefreshables { refreshAfterGameEnd(results) } // Refresh the UI or game state with the results.
    }

    /**
     * Calculates the results of the current game.
     * This method evaluates each player's hand to determine their score based on poker hand rankings
     * and returns a map associating each player with their score.
     *
     * @return A map where each key is a Player object and each value is the score of their hand.
     */
     fun calcResults(): Map<Player, Int> {
        val game = rootService.currentGame
        checkNotNull(game)

        // Associate each player with their score by evaluating their hand
        val mapOfPlayerScore = game.players.associateWith {
            val playerHand = (it.openCards + it.hiddenCards).toMutableList() // Combine open and hidden cards
            evaluateHand(playerHand) // Evaluate the combined hand to determine the score
        }

        return mapOfPlayerScore // Return the map of players and their scores
    }

    /**
     * Evaluates a poker hand and returns a score based on the hand's value.
     *
     * @param cards A mutable list of cards representing the player's hand.
     * @return An integer score representing the hand's value, where higher scores correspond to stronger hands.
     */
     fun evaluateHand(cards: MutableList<Card>): Int {
        val sortedCards = cards.sortedBy { it.value.getNumericValue() } // Sort the cards by their numeric value.
        val flushCards = cards.all { it.color == cards.first().color } // Check if all cards share the same color.
        // Determine if the cards form a straight by checking if each pair of cards differs by one in their values.
        val straightCards = sortedCards.zipWithNext { firstCard, secondCard ->
            secondCard.value.getNumericValue() - firstCard.value.getNumericValue()
        }.all { it == 1 }
        // Group the cards by their value and sort the groups by size to identify pairs, threes, and fours.
        val groupValue = sortedCards.groupBy { it.value }.map { it.value.size }.sortedDescending()

        // Determine the hand's value based on the official poker rules from wikipedia
        return when {
            straightCards && flushCards && sortedCards.last().value == CardValue.ACE -> 10 // Royal Flush
            flushCards && straightCards -> 9 // Straight Flush
            groupValue == listOf(4, 1) -> 8 // Four of a Kind
            groupValue == listOf(3, 2) -> 7 // Full House
            flushCards -> 6 // Flush
            straightCards -> 5 // Straight
            groupValue == listOf(3, 1, 1) -> 4 // Three of a Kind
            groupValue == listOf(2, 2, 1) -> 3 // Two Pair
            groupValue.first() == 2 -> 2 // One Pair, corrected to properly rank Two Pair and One Pair.
            else -> 1 // High Card
        }
    }


    /**
     * Advances to the next player's turn
     */
    fun nextPlayer() {
        // get currentGame
        val game = rootService.currentGame
        checkNotNull(game)

        // reset has shifted for the currentPlayer
        game.players[game.currentPlayer].hasShifted = false

        //decrement the rounds counter
        if (game.players.size - 1 == game.currentPlayer ) game.rounds--

        //check if the set rounds are reached and endGame if true
        if (game.players.size - 1 == game.currentPlayer && game.rounds == 0){
            println("Game Over")
            endGame()
        }

        // move to the next player
        game.currentPlayer = (game.currentPlayer + 1) % game.players.size

    }

    /**
     * Creates a full deck of cards
     *
     * @return A MutableList of Card objects representing a full deck
     */
    private fun createFullDeck(): MutableList<Card> {
        val deck = mutableListOf<Card>()
        for (color in CardSuit.values()) {
            for (value in CardValue.values()) {
                deck.add(Card(color, value))
            }
        }
        return deck
    }
}
