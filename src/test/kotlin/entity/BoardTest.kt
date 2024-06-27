package entity

import kotlin.test.*

/**
 * A test suite for the Board class, designed to ensure that the game board initializes correctly
 * and behaves as expected when interacting with its properties.
 *
 * This class tests the Board's ability to manage its various card piles, including the middle cards,
 * both discard piles, and the draw pile, ensuring they start empty and can be modified correctly.
 */
class BoardTest {
    private var board = Board()

    /**
     * Tests the initial state of the Board to verify that all card piles are empty.
     *
     * This test ensures that the game board starts with no cards in any of its piles,
     * indicating a correct initialization state for the start of a game.
     */
    @Test
    fun initializeTest() {
        assertTrue(board.middleCards.isEmpty(), "Middle cards should be empty on initialization.")
        assertTrue(board.leftdiscardPile.isEmpty(), "Left discard pile should be empty on initialization.")
        assertTrue(board.rightDiscardPile.isEmpty(), "Right discard pile should be empty on initialization.")
        assertTrue(board.drawPile.isEmpty(), "Draw pile should be empty on initialization.")
    }

    /**
     * Tests the ability of the Board to store cards in each of its piles.
     *
     * This test adds a single card to each of the board's piles and then verifies
     * that each pile contains exactly one card, ensuring that cards can be added
     * and stored correctly.
     */
    @Test
    fun storeTest() {
        board.drawPile.add(Card(CardSuit.HEARTS, CardValue.ACE))
        board.middleCards.add(Card(CardSuit.HEARTS, CardValue.ACE))
        board.leftdiscardPile.add(Card(CardSuit.HEARTS, CardValue.ACE))
        board.rightDiscardPile.add(Card(CardSuit.HEARTS, CardValue.ACE))

        assertEquals(1, board.drawPile.size, "Draw pile should contain one card ")
        assertEquals(1, board.middleCards.size, "Middle cards should contain one card ")
        assertEquals(1, board.leftdiscardPile.size, "Left discard pile should contain one card ")
        assertEquals(1, board.rightDiscardPile.size, "Right discard pile should contain one card ")
    }
}
