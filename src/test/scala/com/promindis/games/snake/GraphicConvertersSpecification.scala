package com.promindis.games.snake

import org.specs2.Specification
import com.promindis.games.snake.GraphicConverters._


class GraphicConvertersSpecification extends Specification{ def is =
  "Graphics converters specification " ^
  p^
  "from point to display should " ^
  "convert properly worldPoint to screen point " ! e1


  def e1 = {
    diplayPointFrom(WorldLocation(5, 10)).x.should(beEqualTo(50))
    diplayPointFrom(WorldLocation(5, 10)).y.should(beEqualTo(100))
    diplayPointFrom(WorldLocation(5, 10)).width.should(beEqualTo(100))
  }





}