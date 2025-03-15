package io.github.mercurievv.minuscles.tuples.transformers

import scala.util.Random

object compiletime:

  import scala.quoted.*

  inline def swapValues: (Int, Int) = ${ swapValuesCode }

  def swapValuesCode(using Quotes): Expr[(Int, Int)] = {
    val n1 = 1 + (Random.nextInt() % 8).abs
    val n2 = 1 + (Random.nextInt() % 8).abs
    val n2f = if (n1 == n2) { (n2 + 1) % 8 }
    else
      n2
    Expr(
      (
        n1,
        n2f,
      )
    )
  }
