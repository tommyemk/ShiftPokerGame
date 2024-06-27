package service

import kotlin.test.*
import entity.*
/**
 * Test suite for [PlayerActionService] functionality.
 *
 * Validates shifting cards, swapping cards, and passing turns within a simulated game environment.
 */
class PlayerServiceTest {
    private lateinit var rootService: RootService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var game: ShiftPokerGame
    private lateinit var testRefreshable: TestRefreshable

    /**
     * Sets up the testing environment before each test.
     *
     * Initializes the game with predefined players and card decks.
     */
    @BeforeTest
    fun setUp() {

        rootService = RootService()
        game = ShiftPokerGame(2, Board()).apply {
            players = listOf(Player("Tommy", false), Player("Eddy", false))
            players[0].openCards.addAll(
                listOf(
                    Card(CardSuit.CLUBS, CardValue.FIVE),
                    Card(CardSuit.DIAMONDS, CardValue.SIX),
                    Card(CardSuit.SPADES, CardValue.SEVEN)
                )
            )
            players[1].openCards.addAll(
                listOf(
                    Card(CardSuit.HEARTS, CardValue.EIGHT),
                    Card(CardSuit.CLUBS, CardValue.NINE),
                    Card(CardSuit.DIAMONDS, CardValue.TEN)
                )
            )
            board.drawPile = mutableListOf(Card(CardSuit.HEARTS, CardValue.ACE), Card(CardSuit.CLUBS, CardValue.KING))
            board.middleCards = mutableListOf(
                Card(CardSuit.SPADES, CardValue.TWO),
                Card(CardSuit.DIAMONDS, CardValue.THREE),
                Card(CardSuit.HEARTS, CardValue.FOUR)
            )
        }
        rootService.currentGame = game
        playerActionService = rootService.playerService
        testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)

    }

    /**
     * Tests shifting cards to the left and validates the state of the game afterward.
     */
    @Test
    fun shiftLeftTest() {
        playerActionService.shift(left = true)
        assertEquals(CardValue.KING, game.board.middleCards[2].value)
        assertEquals(1, game.board.leftdiscardPile.size)
        assertTrue(game.players[0].hasShifted)
        assertTrue(testRefreshable.refreshAfterShiftCard)
        testRefreshable.reset()
    }
    /**
     * Tests shifting cards to the right and validates the state of the game afterward.
     */
    @Test
    fun shiftRightTest() {
        playerActionService.shift(left = false)

        assertEquals(CardValue.KING, game.board.middleCards[0].value)
        assertEquals(1, game.board.rightDiscardPile.size)
        assertTrue(game.players[0].hasShifted)
        assertTrue(testRefreshable.refreshAfterShiftCard)
        testRefreshable.reset()
    }
    /**
     * Tests that an exception is thrown if trying to shift cards when it's not allowed.
     */
    @Test
    fun shiftExceptionTest() {
        game.players[game.currentPlayer].hasShifted = true
        assertFailsWith<IllegalStateException> {
            playerActionService.shift(left = true)
        }
    }
    /**
     * Tests swapping all open cards with the middle cards and validates game state changes.
     */
    @Test
    fun swapAllTest() {
        game.players[game.currentPlayer].hasShifted = true


        val originalMiddleCards = ArrayList(game.board.middleCards)
        val originalPlayerOpenCards = ArrayList(game.players[game.currentPlayer].openCards)


        playerActionService.swapAll()


        assertEquals(originalMiddleCards, game.players[0].openCards)
        assertEquals(originalPlayerOpenCards, game.board.middleCards)


        assertEquals(1, game.currentPlayer)
        assertTrue(testRefreshable.refreshAfterSwapCard)
        assertTrue(testRefreshable.refreshAfterNextPlayer)
        testRefreshable.reset()
    }
    /**
     * Tests passing the turn and validates the transition to the next player.
     */
    @Test
    fun passTest(){
        game.players[game.currentPlayer].hasShifted = true
        val originalCurrentPlayerIndex = game.currentPlayer
        playerActionService.pass()
        val expectedNextPlayerIndex = (originalCurrentPlayerIndex + 1) % game.players.size
        assertEquals(expectedNextPlayerIndex, game.currentPlayer)
        assertTrue(testRefreshable.refreshAfterNextPlayer)
        testRefreshable.reset()
    }

    /**
     * Validates that swapping all cards fails when the player has not shifted cards yet.
     */
    @Test
    fun swapAllFailsWhenNotShifted() {
        assertFailsWith<IllegalStateException> {
            playerActionService.swapAll()
        }

    }
    /**
     * Validates that passing the turn fails when the player has not shifted cards yet.
     */
    @Test
    fun passFailsWhenNotShifted() {

        game.players[game.currentPlayer].hasShifted = false


        assertFailsWith<IllegalStateException> {
            playerActionService.pass()
        }
    }

    /**
     * Tests swapping a single card between player's hand and the middle cards successfully.
     */
    @Test
    fun swapCardsSuccessfully() {

        game.players[game.currentPlayer].hasShifted = true


        val handIndex = 0
        val middleIndex = 1


        val originalPlayerCard = game.players[game.currentPlayer].openCards[handIndex]
        val originalMiddleCard = game.board.middleCards[middleIndex]


        playerActionService.swap(handIndex, middleIndex)


        assertEquals(originalMiddleCard, game.players[0].openCards[handIndex])
        assertEquals(originalPlayerCard, game.board.middleCards[middleIndex])


        assertEquals(1, game.currentPlayer)
        assertTrue(testRefreshable.refreshAfterSwapCard)
        assertTrue(testRefreshable.refreshAfterNextPlayer)
        testRefreshable.reset()
    }

    /**
     * Validates that attempting to swap cards fails when the player has not shifted cards yet.
     */
    @Test
    fun swapFailsWhenNotShifted() {
        assertFailsWith<IllegalStateException> {
            playerActionService.swap(0, 1)
        }
    }
    /**
     * Validates that attempting to swap cards with invalid indices throws an exception.
     */
    @Test
    fun swapFailsWithInvalidIndexes() {

        game.players[game.currentPlayer].hasShifted = true


        assertFailsWith<IndexOutOfBoundsException> {
            playerActionService.swap(-1, 3)
        }
    }
}