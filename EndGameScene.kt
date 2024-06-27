package view

import entity.Player
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * Represents the scene displayed at the end of the game, showing final results.
 * @constructor Initializes the scene with a background image.
 * @property background The visual element used as the scene's background.
 */
class EndGameMenuScene : MenuScene(background = ImageVisual("GameEnd.jpg")),
    Refreshable {

    private val endResultLabel = Label(
        width = 500,
        height = 500,
        posX = 500,
        posY = -200,
        text = "Game Over",
        font = Font(size = 55, color = Color.WHITE)
    )
    private val player1ResultLabel = Label(
        width = 500,
        height = 500,
        posX = 20,
        posY = -100,
        text = "Player 1: 0",
        font = Font(size = 35, color = Color.WHITE)
    )
    private val player2ResultLabel = Label(
        width = 500,
        height = 500,
        posX = 20,
        posY = 0,
        text = "Player 2: 0",
        font = Font(size = 35, color = Color.WHITE)
    )
    private val player3ResultLabel = Label(
        width = 500,
        height = 500,
        posX = 20,
        posY = 100,
        text = "Player 3: 0",
        font = Font(size = 35, color = Color.WHITE)
    )
    private val player4ResultLabel = Label(
        width = 500,
        height = 500,
        posX = 20,
        posY = 200,
        text = "Player 4: 0",
        font = Font(size = 35, color = Color.WHITE)
    )

    /**
     * Updates the UI labels with the game results after the game ends.
     *
     * Sorts players by their scores in descending order and updates labels to display each player's name and hand ranking.
     * - The labels for 1st and 2nd place are always updated since there are at least 2 players in the game.
     * - The labels for 3rd and 4th place are updated only if there are enough players; otherwise, they display "N/A".
     *
     * @param result A map associating each player with their score.
     */
    override fun refreshAfterGameEnd(result: Map<Player, Int>) {
        val sortedResults = result.entries.sortedByDescending { it.value }

        player1ResultLabel.text = "1st: ${sortedResults[0].key.name} - Hand Ranking: ${sortedResults[0].value}"
        player2ResultLabel.text = "2nd: ${sortedResults[1].key.name} - Hand Ranking: ${sortedResults[1].value}"

        if (sortedResults.size > 2) {
            player3ResultLabel.text = "3rd: ${sortedResults[2].key.name} - Hand Ranking: ${sortedResults[2].value}"
        } else {
            player3ResultLabel.text = "3rd: N/A"
        }

        if (sortedResults.size > 3) {
            player4ResultLabel.text = "4th: ${sortedResults[3].key.name} - Hand Ranking: ${sortedResults[3].value}"
        } else {
            player4ResultLabel.text = "4th: N/A"
        }
    }

    init {
        addComponents(
            endResultLabel,
            player1ResultLabel,
            player2ResultLabel,
            player3ResultLabel,
            player4ResultLabel
        )
    }
}