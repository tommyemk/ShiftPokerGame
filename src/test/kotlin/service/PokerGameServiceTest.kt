package service

import entity.*
import kotlin.test.*

/**
 * Test suite for [PokerGameService] functionality.
 */
class PokerGameServiceTest {

    private lateinit var rootService: RootService
    private lateinit var pokerGameService: PokerGameService
    private lateinit var testRefreshable : TestRefreshable
    /**
     * Sets up the testing environment before each test.
     *
     * Initializes the game with predefined players and card decks.
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        pokerGameService = rootService.pokerGameService
        testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
    }
    /**
     * Tests starting a game with valid parameters and validates the state of the game afterward.
     */
    @Test
    fun testStartGameWithValidParameters() {
        val players = listOf("Tommy", "Eddy")
        val rounds = 5
        pokerGameService.startGame(players, rounds)
        val currentGame = rootService.currentGame

        checkNotNull(currentGame)
        assertEquals(rounds, currentGame.rounds)
        assertEquals(players.size, currentGame.players.size)
        currentGame.players.forEach { player ->
            assertEquals(3, player.openCards.size)
            assertEquals(2, player.hiddenCards.size)
        }
        assertEquals(3, currentGame.board.middleCards.size)
        assertTrue(currentGame.board.drawPile.isNotEmpty())
        assertTrue(testRefreshable.refreshAfterStartGame)
        testRefreshable.reset()
    }

    /**
     * Tests starting a game with invalid parameters and validates the state of the game afterward.
     */
        @Test
        fun testStartGameWithInvalidNumberOfPlayers() {
            val players = listOf("Tommy")
            val rounds = 5

            assertFailsWith<IllegalArgumentException> {
                pokerGameService.startGame(players, rounds)
            }
        }

    /**
     * Tests starting a game with invalid parameters and validates the state of the game afterward.
     */
        @Test
        fun testStartGameWithInvalidNumberOfRounds() {
            val players = listOf("Tommy", "Eddy")
            val rounds = 8

            assertFailsWith<IllegalArgumentException> {
                pokerGameService.startGame(players, rounds)
            }
        }

    /**
     * Tests shifting cards to the left and validates the state of the game afterward.
     */

        @Test
        fun testNextPlayerCorrectly() {
            pokerGameService.startGame(listOf("Tommy", "Eddy"), 2)
            val game = rootService.currentGame
            checkNotNull(game)
            val initialPlayerIndex = game.currentPlayer
            pokerGameService.nextPlayer()
            val nextPlayerIndex = game.currentPlayer


            assertEquals((initialPlayerIndex + 1) % 2, nextPlayerIndex)


            assertFalse(
                game.players[nextPlayerIndex].hasShifted
            )
        }

    /**
     * Tests shifting cards to the left and validates the state of the game afterward.
     */


        @Test
        fun testNextPlayerDecrementsRoundsOnLastPlayer() {
            pokerGameService.startGame(listOf("Tommy", "Eddy"), 2)
            val game = rootService.currentGame
            checkNotNull(game)
            game.currentPlayer = game.players.size - 1
            val initialRounds = game.rounds
            pokerGameService.nextPlayer()

            assertEquals(initialRounds - 1, game.rounds)
        }

    /**
     * Tests shifting cards to the left and validates the state of the game afterward.
     */

    @Test
    fun testEvaluateHandWithPair() {
        val players = listOf("Tommy", "Eddy")
        val rounds = 5
        pokerGameService.startGame(players, rounds)
        val cards = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.DIAMONDS, CardValue.THREE),
            Card(CardSuit.CLUBS, CardValue.FOUR),
            Card(CardSuit.HEARTS, CardValue.FIVE)
        )
        val score = pokerGameService.evaluateHand(cards)
        assertEquals(2, score)
    }

    @Test
    fun testEvaluateHandWithTwoPair() {
        val cards = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.SPADES, CardValue.TWO),
            Card(CardSuit.DIAMONDS, CardValue.THREE),
            Card(CardSuit.CLUBS, CardValue.THREE),
            Card(CardSuit.HEARTS, CardValue.FIVE)
        )
        val score = pokerGameService.evaluateHand(cards)
        assertEquals(3, score)
    }

    @Test
    fun testEvaluateHandWithStraight() {
        val cards = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.SPADES, CardValue.THREE),
            Card(CardSuit.DIAMONDS, CardValue.FOUR),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.SIX)
        )
        val score = pokerGameService.evaluateHand(cards)
        assertEquals(5, score)
    }

    /**
     * Tests shifting cards to the left and validates the state of the game afterward.
     */
    @Test
    fun testCalcResultsWithDifferentHands() {
        val players = listOf("Tommy", "Eddy")
        val rounds = 5
        pokerGameService.startGame(players, rounds)
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)

        currentGame.players[0].openCards.clear()
        currentGame.players[0].hiddenCards.clear()
        currentGame.players[0].openCards.addAll(listOf(
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.HEARTS, CardValue.THREE),
            Card(CardSuit.HEARTS, CardValue.FOUR)
        ))
        currentGame.players[0].hiddenCards.addAll(listOf(
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.SIX)
        ))


        currentGame.players[1].openCards.clear()
        currentGame.players[1].hiddenCards.clear()
        currentGame.players[1].openCards.addAll(listOf(
            Card(CardSuit.CLUBS, CardValue.TEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.QUEEN)
        ))
        currentGame.players[1].hiddenCards.addAll(listOf(
            Card(CardSuit.SPADES, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.ACE)
        ))

        val results = pokerGameService.calcResults()


        assertEquals(9, results[currentGame.players[0]])


        assertEquals(5, results[currentGame.players[1]])

        pokerGameService.endGame()
        assertTrue(testRefreshable.refreshAfterGameEnd)
        testRefreshable.reset()
    }

}
