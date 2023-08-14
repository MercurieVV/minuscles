package io.github.mercurievv.minuscles.tuples.plens

import io.github.mercurievv.minuscles.tuples.plens.implicits._
object Test {
  val a: (Double, Int, Boolean) = (4.0, 5, true)
  val b = a.at(2).modify(_.toString)
  val c = a.mod(2)(_.toString)
}
