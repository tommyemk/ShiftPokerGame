package view

import service.RootService
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ImageVisual
import entity.*
import service.*
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.ProgressBar
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * Scene for the poker game.
 */
@Suppress("TooManyFunctions","LargeClass")
class PokerGameScene(private val rootService: RootService) :
    BoardGameScene(background = ImageVisual("pokergamebg.jpg")), Refreshable {
    private var cardView1: CardView? = null
    private var cardView2: CardView? = null
    private var hiddenCardList: MutableList<CardView?> = mutableListOf()
    private val passButton = Button(
        width = 100, height = 70, posX = 1300, posY = 120, text = "Pass", font = Font(size = 20)
    ).apply {
        isVisible = false
        onMouseClicked = {
            sure.isVisible = true
            yesButton.isVisible = true
            noButton.isVisible = true
            swapAllButton.isVisible = false
            swapOneButton.isVisible = false
            isVisible = false
            for (card in player1HiddenCards) {
                flipBackwards(card)
            }
        }
    }
    private val yesButton = Button(
        width = 100, height = 70, posX = 1250, posY = 250, text = "Yes", font = Font(size = 20)
    ).apply {
        isVisible = false
        onMouseClicked = {
            rootService.playerService.pass()

        }
    }

    private val noButton = Button(
        width = 100, height = 70, posX = 1350, posY = 250, text = "No", font = Font(size = 20)
    ).apply {
        isVisible = false
        onMouseClicked = {
            sure.isVisible = false
            yesButton.isVisible = false
            isVisible = false
            swapAllButton.isVisible = true
            swapOneButton.isVisible = true
            for (card in player1HiddenCards) {
                flipForwards(card)
            }
        }
    }

    private val sure = Label(
        width = 500,
        height = 60,
        posX = 1105,
        posY = 200,
        text = "Are you sure?",
        font = Font(size = 30, color = Color.WHITE)
    ).apply {
        isVisible = false
    }

    private val rounds = Label(
        width = 500,
        height = 50,
        posX = 1100,
        posY = 30,
        text = "Rounds Left: ",
        font = Font(size = 40, color = Color.WHITE)
    )
    private val player1Name = Label(
        width = 700, height = 50, posX = 583, posY = 1020, text = "", font = Font(size = 40, color = Color.WHITE)
    )
    private val player2Name = Label(
        width = 700, height = 50, posX = 583, posY = 30, text = "Tommy", font = Font(size = 40, color = Color.WHITE)
    ).apply { isVisible = false }

    private var middleProgressBar = ProgressBar(
        width = 130, height = 30, posX = 870, posY = 620, progress = 0.30, barColor = Color.BLUE
    ).apply {
        isVisible = false
        onMouseClicked = {
            middleProgress()
        }
    }
    private var confirmSwapOne = Button(
        width = 80,
        height = 50,
        posX = 890,
        posY = 670,
        text = "Confirm",
    ).apply {
        isVisible = false
        onMouseClicked = {
            indexSwapOne()
            middleProgressBar.isVisible = false
            openProgressBar.isVisible = false
            isVisible = false
        }
    }

    private fun indexSwapOne() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        val index1 = player1OpenCards.indexOf(cardView2)
        println("index1: $index1")
        val index2 = middleCardsView.indexOf(cardView1)
        println("index2: $index2")
        rootService.playerService.swap(index1, index2)
        cardView1?.scale = 1.0
        cardView2?.scale = 1.0
    }

    private fun middleProgress() {
        middleProgressBar.onMouseClicked = {
            middleProgressBar.progress = when (middleProgressBar.progress) {
                1.0 -> 0.30
                0.30 -> 0.70
                else -> 1.0
            }
            when (middleProgressBar.progress) {
                0.30 -> middleProgressBar.barColor = Color.BLUE
                0.70 -> middleProgressBar.barColor = Color.GREEN
                1.0 -> middleProgressBar.barColor = Color.RED
            }
            when (middleProgressBar.progress) {
                0.30 -> {
                    middleCardsView.elementAt(0).scale = 1.15
                    cardView1 = middleCardsView.elementAt(0)
                    middleCardsView.elementAt(1).scale = 1.0
                    middleCardsView.elementAt(2).scale = 1.0
                }

                0.70 -> {
                    middleCardsView.elementAt(1).scale = 1.15
                    cardView1 = middleCardsView.elementAt(1)
                    middleCardsView.elementAt(0).scale = 1.0
                    middleCardsView.elementAt(2).scale = 1.0
                }

                1.0 -> {
                    middleCardsView.elementAt(2).scale = 1.15
                    cardView1 = middleCardsView.elementAt(2)
                    middleCardsView.elementAt(0).scale = 1.0
                    middleCardsView.elementAt(1).scale = 1.0
                }
            }
        }
    }

    private var openProgressBar = ProgressBar(
        width = 130, height = 30, posX = 870, posY = 750, progress = 0.30, barColor = Color.BLUE
    ).apply {
        isVisible = false
        onMouseClicked = {
            openProgress()
        }
    }

    private fun openProgress() {
        openProgressBar.onMouseClicked = {
            openProgressBar.progress = when (openProgressBar.progress) {
                1.0 -> 0.30
                0.30 -> 0.70
                else -> 1.0
            }
            when (openProgressBar.progress) {
                0.30 -> openProgressBar.barColor = Color.BLUE
                0.70 -> openProgressBar.barColor = Color.GREEN
                1.0 -> openProgressBar.barColor = Color.RED
            }
            when (openProgressBar.progress) {
                0.30 -> {
                    player1OpenCards.elementAt(0).scale = 1.15
                    cardView2 = player1OpenCards.elementAt(0)
                    player1OpenCards.elementAt(1).scale = 1.0
                    player1OpenCards.elementAt(2).scale = 1.0
                }

                0.70 -> {
                    player1OpenCards.elementAt(1).scale = 1.15
                    cardView2 = player1OpenCards.elementAt(1)
                    player1OpenCards.elementAt(0).scale = 1.0
                    player1OpenCards.elementAt(2).scale = 1.0
                }

                1.0 -> {
                    player1OpenCards.elementAt(2).scale = 1.15
                    cardView2 = player1OpenCards.elementAt(2)
                    player1OpenCards.elementAt(0).scale = 1.0
                    player1OpenCards.elementAt(1).scale = 1.0
                }
            }
        }
    }

    private val cardMap = BidirectionalMap<Card, CardView>()
    private var drawPileView = CardStack<CardView>(
        posX = 1300, posY = 800, width = 130, height = 200, visual = ColorVisual(255, 255, 255, 50)
    ).apply { rotation = 90.0 }

    private val middleCardsView = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = 10,
        alignment = Alignment.CENTER,
        width = 420,
        height = 200,
        posX = 720,
        posY = 400,
        visual = ColorVisual(0, 0, 0, 0)
    )

    private val player1HiddenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = -60,
        alignment = Alignment.CENTER,
        width = 260,
        height = 200,
        posX = 450,
        posY = 800,
        visual = ColorVisual(0, 0, 0, 0)
    )

    private val player1OpenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = 10,
        alignment = Alignment.CENTER,
        width = 420,
        height = 200,
        posX = 720,
        posY = 800,
        visual = ColorVisual(0, 0, 0, 0)
    )

    private val player2HiddenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = -60,
        alignment = Alignment.CENTER,
        width = 260,
        height = 200,
        posX = 450,
        posY = 100,
        visual = ColorVisual(0, 0, 0, 0)
    )

    private val player2OpenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = 10,
        alignment = Alignment.CENTER,
        width = 420,
        height = 200,
        posX = 720,
        posY = 100,
        visual = ColorVisual(0, 0, 0, 0)
    )

    private val player3OpenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = 10,
        alignment = Alignment.CENTER,
        width = 420,
        height = 200,
        posX = 15,
        posY = 500,
        visual = ColorVisual(0, 0, 0, 0)
    ).apply {
        rotation = 90.0
    }

    private val player3HiddenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = -60,
        alignment = Alignment.CENTER,
        width = 260,
        height = 200,
        posX = 95,
        posY = 150,
        visual = ColorVisual(0, 0, 0, 0)
    ).apply { rotation = 90.0 }

    private val player4OpenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = 10,
        alignment = Alignment.CENTER,
        width = 420,
        height = 200,
        posX = 1400,
        posY = 500,
        visual = ColorVisual(0, 0, 0, 0)
    ).apply { rotation = 90.0 }

    private val player4HiddenCards = LinearLayout<CardView>(
        orientation = Orientation.HORIZONTAL,
        spacing = -60,
        alignment = Alignment.CENTER,
        width = 260,
        height = 200,
        posX = 1480,
        posY = 150,
        visual = ColorVisual(0, 0, 0, 0)
    ).apply { rotation = 90.0 }

    private val leftDiscardPileView = CardStack<CardView>(
        posX = 450, posY = 400, width = 130, height = 200, visual = ColorVisual(255, 255, 255, 50)
    )

    private val rightDiscardPileView = CardStack<CardView>(
        posX = 1280, posY = 400, width = 130, height = 200, visual = ColorVisual(255, 255, 255, 50)
    )

    private val leftArrow = Button(
        width = 100, height = 100, posX = 600, posY = 450, visual = ImageVisual("leftArrow.png")
    ).apply {
        onMouseClicked = {
            rootService.playerService.shift(left = true)
        }
    }

    private val rightArrow = Button(
        width = 100, height = 100, posX = 1160, posY = 450, visual = ImageVisual("rightArrow.png")
    ).apply {
        onMouseClicked = {
            rootService.playerService.shift(left = false)
        }
    }

    private val swapAllButton = Button(
        width = 100,
        height = 70,
        posX = 200,
        posY = 860,
        text = "Swap All",

        ).apply {
        isVisible = false
        onMouseClicked = {
            rootService.playerService.swapAll()
        }
    }

    private val swapOneButton = Button(
        width = 100,
        height = 70,
        posX = 1530,
        posY = 860,
        text = "Swap One",
    ).apply {
        isVisible = false
        onMouseClicked = {
            isVisible = false
            confirmSwapOne.isVisible = true
            middleProgressBar.isVisible = true
            openProgressBar.isVisible = true
            swapAllButton.isVisible = false
            isVisible = false

            if (middleProgressBar.progress == 0.30) {
                middleCardsView.elementAt(0).scale = 1.15
                cardView1 = middleCardsView.elementAt(0)
            }

            if (openProgressBar.progress == 0.30) {
                player1OpenCards.elementAt(0).scale = 1.15
                cardView2 = player1OpenCards.elementAt(0)
            }
        }
    }

    override fun refreshAfterShiftCard(left: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        when (left) {
            true -> {
                val card = game.board.leftdiscardPile[0]
                middleCardsView.clear()
                leftDiscardPileView.add(cardMap.forward(card))
                leftDiscardPileView.find { it == cardMap.forward(card) }?.showFront()
                drawPileView.find { it == cardMap.forward(card) }?.let { drawPileView.remove(it) }
                updateMiddleCards(middleCardsView)
                swapAllButton.isVisible = game.players[game.currentPlayer].hasShifted
                swapOneButton.isVisible = game.players[game.currentPlayer].hasShifted
                passButton.isVisible = game.players[game.currentPlayer].hasShifted
                rightArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)
                leftArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)

            }

            false -> {
                val card = game.board.rightDiscardPile[0]
                middleCardsView.clear()
                rightDiscardPileView.add(cardMap.forward(card))
                rightDiscardPileView.find { it == cardMap.forward(card) }?.showFront()
                drawPileView.find { it == cardMap.forward(card) }?.let { drawPileView.remove(it) }
                updateMiddleCards(middleCardsView)
                swapAllButton.isVisible = game.players[game.currentPlayer].hasShifted
                swapOneButton.isVisible = game.players[game.currentPlayer].hasShifted
                passButton.isVisible = game.players[game.currentPlayer].hasShifted
                rightArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)
                leftArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)
            }
        }
    }

    override fun refreshAfterSwapCard(all: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        when (all) {
            true -> {
                println("ok")
                swapAllTrue()
            }

            else -> {
                swapAllFalse()
                println(game.currentPlayer)
            }
        }
    }


    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        cardMap.clear()
        val cardImageLoader = CardImageLoader()
        initilizeDrawPile(drawPileView, cardImageLoader)
        initializeMiddleCards(middleCardsView, cardImageLoader)
        initializeDiscardPile(leftDiscardPileView, cardImageLoader, game.board.leftdiscardPile)
        initializeDiscardPile(rightDiscardPileView, cardImageLoader, game.board.rightDiscardPile)
        when (game.players.size) {
            2 -> {
                initiliazeOpenCards(player1OpenCards, cardImageLoader, game.players[0].openCards)
                initiliazaHiddenCards(player1HiddenCards, cardImageLoader, game.players[0].hiddenCards)
                initiliazeOpenCards(player2OpenCards, cardImageLoader, game.players[1].openCards)
                initiliazaHiddenCards(player2HiddenCards, cardImageLoader, game.players[1].hiddenCards)
                val cardList = game.players[game.currentPlayer].hiddenCards
                for (card in cardList) {
                    cardMap.forward(card).showFront()
                }
            }

            3 -> {
                initiliazeOpenCards(player1OpenCards, cardImageLoader, game.players[0].openCards)
                initiliazaHiddenCards(player1HiddenCards, cardImageLoader, game.players[0].hiddenCards)
                initiliazeOpenCards(player2OpenCards, cardImageLoader, game.players[1].openCards)
                initiliazaHiddenCards(player2HiddenCards, cardImageLoader, game.players[1].hiddenCards)
                initiliazeOpenCards(player3OpenCards, cardImageLoader, game.players[2].openCards)
                initiliazaHiddenCards(player3HiddenCards, cardImageLoader, game.players[2].hiddenCards)
                val cardList = game.players[game.currentPlayer].hiddenCards
                for (card in cardList) {
                    cardMap.forward(card).showFront()
                }
            }

            4 -> {
                initiliazeOpenCards(player1OpenCards, cardImageLoader, game.players[0].openCards)
                initiliazaHiddenCards(player1HiddenCards, cardImageLoader, game.players[0].hiddenCards)
                initiliazeOpenCards(player2OpenCards, cardImageLoader, game.players[1].openCards)
                initiliazaHiddenCards(player2HiddenCards, cardImageLoader, game.players[1].hiddenCards)
                initiliazeOpenCards(player3OpenCards, cardImageLoader, game.players[2].openCards)
                initiliazaHiddenCards(player3HiddenCards, cardImageLoader, game.players[2].hiddenCards)
                initiliazeOpenCards(player4OpenCards, cardImageLoader, game.players[3].openCards)
                initiliazaHiddenCards(player4HiddenCards, cardImageLoader, game.players[3].hiddenCards)
                val cardList = game.players[game.currentPlayer].hiddenCards
                for (card in cardList) {
                    cardMap.forward(card).showFront()
                }
            }
        }
        player1Name.text = game.players[game.currentPlayer].name
        rounds.text = "Rounds Left: ${game.rounds}"
        for (card in player1HiddenCards) {
            hiddenCardList.add(card)
        }

    }

    @Suppress("ComplexMethod","LongMethod")
    override fun refreshAfterNextPlayer() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        when (game.players.size) {
            2 -> {
                player1OpenCards.clear()
                for (card in player1HiddenCards) {
                    card.showBack()
                }
                player1HiddenCards.clear()
                player2OpenCards.clear()
                for (card in player2HiddenCards) {
                    card.showBack()
                }
                player2HiddenCards.clear()
                updateOpenAndHiddenCards(2)

            }

            3 -> {
                player1OpenCards.clear()
                for (card in player1HiddenCards) {
                    card.showBack()
                }
                player1HiddenCards.clear()
                player2OpenCards.clear()
                for (card in player2HiddenCards) {
                    card.showBack()
                }
                player2HiddenCards.clear()
                player3OpenCards.clear()
                for (card in player3HiddenCards) {
                    card.showBack()
                }
                player3HiddenCards.clear()
                updateOpenAndHiddenCards(3)
            }

            4 -> {
                player1OpenCards.clear()
                for (card in player1HiddenCards) {
                    card.showBack()
                }
                player1HiddenCards.clear()
                player2OpenCards.clear()
                for (card in player2HiddenCards) {
                    card.showBack()
                }
                player2HiddenCards.clear()
                player3OpenCards.clear()
                for (card in player3HiddenCards) {
                    card.showBack()
                }
                player3HiddenCards.clear()
                player4OpenCards.clear()
                for (card in player4HiddenCards) {
                    card.showBack()
                }
                player4HiddenCards.clear()
                updateOpenAndHiddenCards(4)
            }
        }
        passButton.isVisible = game.players[game.currentPlayer].hasShifted
        swapAllButton.isVisible = game.players[game.currentPlayer].hasShifted
        swapOneButton.isVisible = game.players[game.currentPlayer].hasShifted
        rightArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)
        leftArrow.isVisible = !(game.players[game.currentPlayer].hasShifted)
        sure.isVisible = false
        yesButton.isVisible = false
        noButton.isVisible = false
        confirmSwapOne.isVisible = false
        middleProgressBar.isVisible = false
        openProgressBar.isVisible = false
        for (card in middleCardsView) {
            card.scale = 1.0
        }
        for (card in player1OpenCards) {
            card.scale = 1.0
        }

    }
@Suppress("ComplexMethod","LongMethod")
    private fun updateOpenAndHiddenCards(playerAmount: Int) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        val currentPlayer = game.players[game.currentPlayer]
        when (playerAmount) {
            2 -> {
                for (card in currentPlayer.openCards) {
                    player1OpenCards.add(cardMap.forward(card))
                }
                for (card in currentPlayer.hiddenCards) {
                    player1HiddenCards.add(cardMap.forward(card))
                    cardMap.forward(card).showFront()
                }
                for (card in game.players[(game.currentPlayer + 1) % 2].openCards) {
                    player2OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 1) % 2].hiddenCards) {
                    player2HiddenCards.add(cardMap.forward(card))
                }
            }

            3 -> {
                for (card in currentPlayer.openCards) {
                    player1OpenCards.add(cardMap.forward(card))
                }
                for (card in currentPlayer.hiddenCards) {
                    player1HiddenCards.add(cardMap.forward(card))
                    cardMap.forward(card).showFront()
                }
                for (card in game.players[(game.currentPlayer + 1) % 3].openCards) {
                    player2OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 1) % 3].hiddenCards) {
                    player2HiddenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 2) % 3].openCards) {
                    player3OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 2) % 3].hiddenCards) {
                    player3HiddenCards.add(cardMap.forward(card))
                }
            }

            4 -> {
                for (card in currentPlayer.openCards) {
                    player1OpenCards.add(cardMap.forward(card))
                }
                for (card in currentPlayer.hiddenCards) {
                    player1HiddenCards.add(cardMap.forward(card))
                    cardMap.forward(card).showFront()
                }
                for (card in game.players[(game.currentPlayer + 1) % 4].openCards) {
                    player2OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 1) % 4].hiddenCards) {
                    player2HiddenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 2) % 4].openCards) {
                    player3OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 2) % 4].hiddenCards) {
                    player3HiddenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 3) % 4].openCards) {
                    player4OpenCards.add(cardMap.forward(card))
                }
                for (card in game.players[(game.currentPlayer + 3) % 4].hiddenCards) {
                    player4HiddenCards.add(cardMap.forward(card))
                }
            }
        }
        player1Name.text = game.players[game.currentPlayer].name
        rounds.text = "Rounds Left: ${game.rounds}"
    }

    private fun initilizeDrawPile(stack: CardStack<CardView>, cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        stack.clear()
        for (card in game.board.drawPile) {
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stack.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun initializeMiddleCards(stack: LinearLayout<CardView>, cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        stack.clear()
        for (card in game.board.middleCards) {
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            cardView.showFront()
            stack.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun updateMiddleCards(stack: LinearLayout<CardView>) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        val safeDrawPileView = drawPileView.toMutableList()
        drawPileView.clear()
        stack.clear()
        for (card in game.board.middleCards) {
            if (cardMap.forward(card) in safeDrawPileView) {
                safeDrawPileView.find { it == cardMap.forward(card) }?.let { safeDrawPileView.remove(it) }
            }
            stack.add(cardMap.forward(card))
            cardMap.forward(card).showFront()
        }
        for (card in safeDrawPileView) {
            drawPileView.add(card)
        }
    }

    private fun initiliazeOpenCards(
        stack: LinearLayout<CardView>, cardImageLoader: CardImageLoader, list: MutableList<Card>
    ) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        stack.clear()
        when (list) {
            game.players[0].openCards -> {
                for (card in game.players[0].openCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showFront()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[1].openCards -> {
                for (card in game.players[1].openCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showFront()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[2].openCards -> {
                for (card in game.players[2].openCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showFront()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[3].openCards -> {
                for (card in game.players[3].openCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showFront()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }
        }
    }

    private fun initiliazaHiddenCards(
        stack: LinearLayout<CardView>, cardImageLoader: CardImageLoader, list: MutableList<Card>
    ) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        stack.clear()
        when (list) {
            game.players[0].hiddenCards -> {
                for (card in game.players[0].hiddenCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showBack()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[1].hiddenCards -> {
                for (card in game.players[1].hiddenCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showBack()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[2].hiddenCards -> {
                for (card in game.players[2].hiddenCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showBack()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.players[3].hiddenCards -> {
                for (card in game.players[3].hiddenCards) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    cardView.showBack()
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }
        }

    }

    private fun initializeDiscardPile(
        stack: CardStack<CardView>, cardImageLoader: CardImageLoader, list: MutableList<Card>
    ) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        stack.clear()
        when (list) {
            game.board.leftdiscardPile -> {
                for (card in game.board.leftdiscardPile) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }

            game.board.rightDiscardPile -> {
                for (card in game.board.rightDiscardPile) {
                    val cardView = CardView(
                        height = 200,
                        width = 130,
                        front = ImageVisual(cardImageLoader.frontImageFor(card.color, card.value)),
                        back = ImageVisual(cardImageLoader.backImage)
                    )
                    stack.add(cardView)
                    cardMap.add(card to cardView)
                }
            }
        }
    }

    private fun swapAllTrue() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        println("ok")
        val currentPlayer = game.players[game.currentPlayer]
        println(game.currentPlayer)

        middleCardsView.clear()
        player1OpenCards.clear()

        for (card in currentPlayer.openCards) {
            player1OpenCards.add(cardMap.forward(card))

        }
        for (card in game.board.middleCards) {
            println(card)
        }
        for (card in game.board.middleCards) {
            middleCardsView.add(cardMap.forward(card))

        }
    }

    // if false do swap one
    private fun swapAllFalse() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        val currentPlayer = game.players[game.currentPlayer]
        middleCardsView.clear()
        player1OpenCards.clear()
        for (card in currentPlayer.openCards) {
            player1OpenCards.add(cardMap.forward(card))

        }
        for (card in game.board.middleCards) {
            middleCardsView.add(cardMap.forward(card))

        }
        confirmSwapOne.isVisible = false
        middleCardsView.elementAt(0).scale = 1.0
        player1OpenCards.elementAt(0).scale = 1.0
    }

    private fun flipBackwards(card: CardView) {
        this.playAnimation(FlipAnimation(
            gameComponentView = card, fromVisual = card.frontVisual, toVisual = card.backVisual, duration = 1000
        ).apply {
            onFinished = {
                card.currentSide = CardView.CardSide.BACK
            }
        })
    }

    private fun flipForwards(card: CardView) {
        this.playAnimation(FlipAnimation(
            gameComponentView = card, fromVisual = card.backVisual, toVisual = card.frontVisual, duration = 1000
        ).apply {
            onFinished = {
                card.currentSide = CardView.CardSide.FRONT
            }
        })
    }


    init {
        addComponents(
            drawPileView,
            middleCardsView,
            leftDiscardPileView,
            rightDiscardPileView,
            leftArrow,
            rightArrow,
            player1OpenCards,
            player1HiddenCards,
            player2OpenCards,
            player2HiddenCards,
            player3OpenCards,
            player3HiddenCards,
            player4OpenCards,
            player4HiddenCards,
            swapAllButton,
            swapOneButton,
            middleProgressBar,
            openProgressBar,
            confirmSwapOne,
            player1Name,
            player2Name,
            rounds,
            passButton,
            sure,
            yesButton,
            noButton
        )
    }
}