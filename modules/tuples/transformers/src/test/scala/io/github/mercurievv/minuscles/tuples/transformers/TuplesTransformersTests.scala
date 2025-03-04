package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop
import org.typelevel.discipline.Laws

class TuplesTransformersTests extends Laws {
  def rules: RuleSet = new DefaultRuleSet(
    name = "TuplesTransformers Laws",
    parent = None,
    "TuplesTransformers idempotency" -> Prop.forAll { (inp: (Int, (Double, Float), String)) =>
      TuplesTransformersLaws[(Int, (Double, Float), String)].idempotency(inp)
    }
  )
}