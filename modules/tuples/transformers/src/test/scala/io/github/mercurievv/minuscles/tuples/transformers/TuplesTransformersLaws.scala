package io.github.mercurievv.minuscles.tuples.transformers

import cats.syntax.functor._ // For F.map
import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import cats.Applicative

trait TuplesTransformersLaws[InpTuple <: Tuple] {

  inline def logIdentity(inpTuple: InpTuple): Prop =
    inpTuple.tFlatten == inpTuple
}

object TuplesTransformersLaws {
  def apply[InpTuple <: Tuple]: TuplesTransformersLaws[InpTuple] =
    new TuplesTransformersLaws[InpTuple]{}
}
