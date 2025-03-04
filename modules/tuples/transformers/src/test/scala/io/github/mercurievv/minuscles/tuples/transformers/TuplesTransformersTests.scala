package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop
import org.typelevel.discipline.Laws

class TuplesTransformersTests extends Laws {
  def rules: RuleSet = new DefaultRuleSet(
    name = "Logger Laws",
    parent = None,
    "log identity" -> Prop.forAll { (inp: (Int, (Double), String)) =>
      TuplesTransformersLaws.apply.logIdentity(inp)
    }
  )
}