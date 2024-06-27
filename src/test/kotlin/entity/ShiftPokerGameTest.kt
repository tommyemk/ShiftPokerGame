package entity

import kotlin.test.*

/**
 * Tests for the ShiftPokerGame class.
 */
class ShiftPokerGameTest {
    private var game = ShiftPokerGame(7,  Board())

    /**
     * Verifies the initial state of the ShiftPokerGame instance.
     */
    @Test
    fun initializeShiftPokerGameTest() {
        // Checks if the game initializes with the correct number of rounds.
        assertEquals(7, game.rounds, "Game should initialize with 7 rounds.")

        // Ensures the game instance is not null.
        assertNotNull(game, "Game instance should be created.")

        // Verifies the game board is a unique instance.
        val board = Board()
        assertNotEquals(board, game.board, "Game board should be a unique instance.")

        // Confirms the game starts without any players.
        assertTrue(game.players.isEmpty(), "Players list should be empty initially.")

        // Adds players to the game and checks their initial state.
        val playerList: List<Player> = listOf(Player("Tommy", false), Player("Eddy", true))
        game.players = playerList
        assertEquals("Tommy", game.players[0].name, "First player should be Tommy.")
        assertFalse(game.players[0].hasShifted, "Tommy should not have shifted yet.")

        // Checks if the currentPlayer is set to 0 at the start.
        assertEquals(0, game.currentPlayer, "The current player should be the first player at the start.")
    }
}
