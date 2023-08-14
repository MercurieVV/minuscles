package io.github.mercurievv.minuscles.tuples.plens

import implicits._
object Ukildo {
  val a: (Double, Int, Boolean) = (4.0, 5, true)
  val b = a.at(2).modify(_.toString)
}
