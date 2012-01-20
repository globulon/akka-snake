package com.promindis.game.snake.stm

import com.promindis.game.snake.stm.World._
import akka.stm._


object Entities {
  sealed trait Entity

  case class Snake(body: List[WorldLocation], direction: WorldLocation) extends Entity {
    def go(toDirection: WorldLocation): Snake = Snake(body, toDirection)

    def moved: Snake = Snake(ahead::body.take(body.size - 1), direction)

    def grown: Snake = Snake(ahead::body, direction)

    def ahead: WorldLocation = head + direction

    def head: WorldLocation = body.head
  }

  case class Apple(location: WorldLocation) extends Entity

  val snake: Ref[Snake] = Ref(Snake(List(origin), Direction.Right))
  val apple: Ref[Apple] = Ref(Apple(randomLocation()))

  private def reset() {
    snake.set(Snake(List(origin), Direction.Right))
    apple.set(Apple(randomLocation()))
  }

  def updatePositions() {
     atomic{
       val fromSnake: Snake = snake.get()
       val fromApple: Apple = apple.get()
       fromSnake.body match {
          case head::tail if head == fromApple.location =>
              apple.set(Apple(randomLocation()))
              snake.set(fromSnake.grown)
          case head::tail if tail.contains(head) =>
            Game.displayMessage("You lose")
            reset()
          case head::tail if tail.size == World.winLength  =>
            Game.displayMessage("You Win")
            reset()
          case _ => snake.set(fromSnake.moved)
        }
       Game.update(snake.get().body, apple.get().location)
     }
  }

  def updateSnakeDirection(to : WorldLocation) {
    atomic {
      snake.alter(fromPrevious => fromPrevious.go(to))
    }
  }
}
