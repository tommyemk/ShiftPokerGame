package view
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import kotlin.system.exitProcess

/**
 * Menu scene for the poker game.

 */
class PokerMenuScene : MenuScene( background =ImageVisual("HomeScreen1.jpg")) {

    private val pokerLabel = Label(
        width = 500,
        height = 500,
        posX = 720,
        posY = -20,
        text = "Push Poker",
        font = Font(size = 80, color = Color.WHITE),
    )
    private val twoPlayers = Label(
        width = 150,
        height = 35,
        posX = 30,
        posY = 150,
        text = "2",
    ).apply {
        isVisible = false
    }

    val startButton : Button = Button(
        width = 250,
        height = 125,
        posX = 500,
        posY = 900,
        text = "Start",
        font = Font(size = 30)
    ).apply {
        isDisabled = false
    }

    private val exitButton = Button(
        width = 250,
        height = 125,
        posX = 1200,
        posY = 900,
        text = "Exit",
        font = Font(size = 30)
    ).apply {
        isDisabled = false
        onMouseClicked = { exitProcess(0) }
    }

    private val playerNumberBar = ProgressBar(
        width = 240,
        height = 60,
        posX = 530,
        posY = 400,
        progress = 0.30,
        barColor = Color.BLUE
    ).apply {
        onMouseClicked = { progressBar() }
    }
    val roundsNumberBar = ProgressBar(
        width = 240,
        height = 60,
        posX = 1130,
        posY = 400,
        progress = 0.30,
        barColor = Color.BLUE
    ).apply {
        onMouseClicked = { selectRoundsfrom2to7() }
    }
    private val labelRounds = Label(
        width = 150,
        height = 35,
        posX = 1170,
        posY = 350,
        text = "Rounds: 2",
        font = Font(size = 30, color = Color.WHITE)
    )
    private val playerNum = Label(
        width = 150,
        height = 35,
        posX = 570,
        posY = 350,
        text = "Players: 2",
        font = Font(size = 30, color = Color.white)
    )
    @Suppress("ComplexMethod")
    private fun selectRoundsfrom2to7() {
        roundsNumberBar.progress = when (roundsNumberBar.progress) {
            0.30 -> 0.40
            0.40 -> 0.50
            0.50 -> 0.60
            0.60 -> 0.70
            0.70 -> 1.0
            else -> 0.30
        }
        when (roundsNumberBar.progress) {
            0.30 -> roundsNumberBar.barColor = Color.BLUE
            0.40 -> roundsNumberBar.barColor = Color.GREEN
            0.50 -> roundsNumberBar.barColor = Color.RED
            0.60 -> roundsNumberBar.barColor = Color.YELLOW
            0.70 -> roundsNumberBar.barColor = Color.ORANGE
            1.0 -> roundsNumberBar.barColor = Color.CYAN
        }
        when (roundsNumberBar.progress) {
            0.30 -> labelRounds.text = "Rounds: 2"
            0.40 -> labelRounds.text = "Rounds: 3"
            0.50 -> labelRounds.text = "Rounds: 4"
            0.60 -> labelRounds.text = "Rounds: 5"
            0.70 -> labelRounds.text = "Rounds: 6"
            1.0 -> labelRounds.text = "Rounds: 7"
        }
    }

    val p1Input = TextField(
        posX = 830,
        posY = 500,
        height = 50,
        width = 250,
        text = "Player 1",
        prompt = "Player 1",
        font = Font(size = 25)
    )
        .apply {
            onKeyTyped = {
                startButton.isDisabled = this.text.isBlank()
            }
        }

    val p2Input = TextField(
        posX = 830,
        posY = 580,
        height = 50,
        width = 250,
        text = "Player 2",
        prompt = "Player 2",
        font = Font(size = 25)
    )
        .apply {
            onKeyTyped = {
                startButton.isDisabled = this.text.isBlank()
            }
        }
    val p3input = TextField(
        posX = 830,
        posY = 660,
        height = 50,
        width = 250,
        text = "",
        prompt = "Player 3",
        font = Font(size = 25)
    )
        .apply {
            isVisible = false
            onKeyTyped = {
                startButton.isDisabled = this.text.isBlank()
            }
        }
    val p4input = TextField(
        posX = 830,
        posY = 740,
        height = 50,
        width = 250,
        text = "",
        prompt = "Player 4",
        font = Font(size = 25)
    )
        .apply {
            isVisible = false
            onKeyTyped = {
                startButton.isDisabled = this.text.isBlank()
            }
        }

    private fun progressBar() {
        playerNumberBar.onMouseClicked = {
            playerNumberBar.progress = when (playerNumberBar.progress) {
                1.0 -> 0.30
                0.30 -> 0.70
                else -> 1.0
            }

            when (playerNumberBar.progress) {
                0.30 -> playerNumberBar.barColor = Color.BLUE
                0.70 -> playerNumberBar.barColor = Color.GREEN
                1.0 -> playerNumberBar.barColor = Color.RED
            }
            when (playerNumberBar.progress) {
                0.30 -> {
                    p3input.isVisible = false
                    p4input.isVisible = false
                    p3input.text = ""
                    p4input.text = ""
                    playerNum.text = "Players: 2"
                }

                0.70 -> {
                    p3input.isVisible = true
                    p3input.text = "Player 3"
                    p4input.isVisible = false
                    playerNum.text = "Players: 3"
                }

                1.0 -> {
                    p3input.isVisible = true
                    p4input.isVisible = true
                    p4input.text = "Player 4"
                    playerNum.text = "Players: 4"
                }
            }
        }
    }

    init {
        addComponents(
            pokerLabel,
            startButton,
            playerNumberBar,
            p1Input,
            p2Input,
            p3input,
            p4input,
            twoPlayers,
            exitButton,
            roundsNumberBar,
            labelRounds,
            playerNum
        )
    }
}