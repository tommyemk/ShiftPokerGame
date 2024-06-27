package entity
/**
 * Represents a player in the card game, holding information about the player's name,
 * their actions, and the cards they possess.
 *
 * Each player has a set of open and hidden cards that are part of their hand in the game,
 * and a flag indicating whether they have performed a shift action during their turn.
 *
 * @property name The name of the player, used for identification purposes.
 * @property hasShifted A boolean value indicating whether the player has already performed
 *              a shift action during their current turn. This is used to enforce game rules
 *              regarding the sequence of actions a player can take.
 */
class Player(
    val name: String,
    var hasShifted: Boolean,
) {
     var openCards: MutableList<Card> = mutableListOf()
     var hiddenCards: MutableList<Card> = mutableListOf()
}
