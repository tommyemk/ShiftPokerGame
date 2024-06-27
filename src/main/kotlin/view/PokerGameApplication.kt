package view

import entity.Player
import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * The main application for the poker game.

 */
class PokerGameApplication : BoardGameApplication("Push Poker"), Refreshable {
    private val rootService = RootService()
    private val pokerMenuScene = PokerMenuScene()
    private val pokerGameScene = PokerGameScene(rootService)
    private val endGameMenuScene = EndGameMenuScene()

    init {
        rootService.addRefreshables(
            pokerGameScene, endGameMenuScene, this
        )
        registerMenuEvents()
        showMenuScene(pokerMenuScene)
        showGameScene(pokerGameScene)
        show()
    }

    private fun registerMenuEvents() {
        pokerMenuScene.startButton.onMouseClicked = {
            rootService.pokerGameService.startGame(
                listOf(
                    pokerMenuScene.p1Input.text,
                    pokerMenuScene.p2Input.text,
                    pokerMenuScene.p3input.text,
                    pokerMenuScene.p4input.text
                ).filter { it.isNotBlank() }, getRounds()
            )
            hideMenuScene()
        }
    }

    override fun refreshAfterGameEnd(result: Map<Player, Int>) {
        showMenuScene(endGameMenuScene)
    }

    private fun getRounds(): Int {
        return when (pokerMenuScene.roundsNumberBar.progress) {
            0.30 -> 2
            0.40 -> 3
            0.50 -> 4
            0.60 -> 5
            0.70 -> 6
            else -> 7
        }
    }
}