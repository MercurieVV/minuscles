package io.github.mercurievv.minuscles.tuples.transformers

import cats.syntax.functor._ // For F.map
import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import cats.Applicative

trait TuplesTransformersLaws[InpTuple <: Tuple] {

  inline def idempotency(inpTuple: InpTuple): Prop =
    val flatten = inpTuple.toFlatten
    val nested = inpTuple.toNested
    flatten.toNested == nested && nested.toFlatten == flatten
}

object TuplesTransformersLaws {
  def apply[InpTuple <: Tuple]: TuplesTransformersLaws[InpTuple] =
    new TuplesTransformersLaws[InpTuple]{}
}
