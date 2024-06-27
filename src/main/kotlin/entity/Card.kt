package entity

/**
 * This class represnts a card
 * @property color is the suit of the card for example clubs, spades, hearts etc
 * @property value is the value of the card with a range from two till ACE
 */
class Card(val color : CardSuit, val value : CardValue) {
    override fun toString() = "$color$value"
}
