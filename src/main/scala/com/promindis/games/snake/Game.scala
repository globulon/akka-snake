package com.promindis.games.snake

import java.awt.{Dimension, Graphics2D}
import swing.event.KeyPressed
import swing._
import java.awt.event.{ActionEvent, ActionListener}
import event.Key._
import akka.actor.{ActorRef, Actor}
import swing.Dialog._

object Game extends SimpleSwingApplication {
  val driver = BoardDriver()
  val board = new Board(handleFor(driver))
  val state = Entities(driver)


  def handleFor(boardDriver: ActorRef) : (Value) => Unit = {
    (key: Value) => boardDriver ! ReceivedPressed(key)
  }

  class Board(handle: => (Value) => Unit ) extends Panel {
    var doPaint: ((Graphics2D) => Unit) = (onGraphics) => {}
    preferredSize = new Dimension(GraphicConverters.converted(World.width), GraphicConverters.converted(World.heigth))
    focusable = true

    override def paintComponent(onGraphic: Graphics2D) {
      super.paintComponent(onGraphic)
      doPaint(onGraphic)
    }

    listenTo(keys)

    reactions += {
      case KeyPressed(source, key, modifiers, location) =>
        handle(key)
    }

    def apply(snake: List[ScreenLocation], apple: ScreenLocation) {
      def paintPoint(screenLocation: ScreenLocation, color: Color, onGraphics: Graphics2D) {
        onGraphics.setColor(color)
        onGraphics.fillRect(screenLocation.x, screenLocation.y, screenLocation.width, screenLocation.height)
      }

      doPaint = (onGraphics: Graphics2D) => {
        paintPoint(apple, new Color(210, 50, 90), onGraphics)
        snake.foreach {
          paintPoint(_, new Color(15, 160, 70), onGraphics)
        }
      }
      repaint()
    }
  }


  case class ShowMessage(text: String)
  case class ReceivedPressed(keyCode: Value)

  class BoardDriver() extends Actor {
    import GraphicConverters._
    import World._

    val directions = Map[Value, WorldLocation](
      Left -> Direction.Left,
      Right -> Direction.Right,
      Up -> Direction.Up,
      Down -> Direction.Down
    )

    protected def receive = {
      case Updated(snake, apple) =>
        board(converted(snake), converted(apple))
      case ReceivedPressed(key) =>
        state ! UpdateDirection(directions(key))
       case ShowMessage(text) => showMessage(parent = board, message = text)

    }
  }

  object BoardDriver {
    def apply() = Actor.actorOf(new BoardDriver()).start()
  }

  def displayMessage(text: String) {
    driver ! ShowMessage(text)
  }

  def top = new MainFrame {
    title = "Snake"
    contents = new FlowPanel() {
      val timer = new javax.swing.Timer(100, new ActionListener() {
        def actionPerformed(e: ActionEvent) {
          state ! Refresh()
        }
      }).start();
      contents += board
    }
    pack()
  }
}

