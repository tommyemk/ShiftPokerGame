package entity

import kotlin.test.*

/**
 * A test suite for the Player class, ensuring that player instances function correctly within the game.
 *
 * This class contains tests to verify the correct initialization and behavior of Player objects, including
 * the uniqueness of player names, the initial state of player actions and cards, and the ability to modify
 * these states as expected during gameplay.
 */
class PlayerTest {
    private var player1 = Player("Tommy", false)
    private var player2 = Player("Arian", false)

    /**
     * Tests that each player has a unique name.
     *
     * This test ensures that two Player instances do not share the same name, which is important for
     * identifying players within the game.
     */
    @Test
    fun uniqueNamesTest() {
        assertNotEquals(player1.name, player2.name)
    }

    /**
     * Tests the initialization and state manipulation of a Player object.
     *
     * This test verifies that a player's 'hasShifted' status starts as false, can be changed, and that
     * both open and hidden card lists are initialized as empty and can be modified by adding cards.
     */
    @Test
    fun initializeTest() {
        assertFalse(player2.hasShifted)
        player2.hasShifted = true
        assertTrue(player2.hasShifted)

        assertTrue(player1.openCards.isEmpty())
        assertTrue(player1.hiddenCards.isEmpty())

        // Add a card to each of player1's card lists and verify the lists contain exactly one card.
        player1.openCards.add(Card(CardSuit.HEARTS, CardValue.ACE))
        player1.hiddenCards.add(Card(CardSuit.HEARTS, CardValue.ACE))
        assertTrue(player1.openCards.size == 1)
        assertTrue(player1.hiddenCards.size == 1)
    }
}
