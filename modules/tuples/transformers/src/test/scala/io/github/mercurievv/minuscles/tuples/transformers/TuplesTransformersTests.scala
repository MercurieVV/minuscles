package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import io.github.mercurievv.minuscles.tuples.transformers.all.*

class TuplesTransformersTests extends Laws {
  type TestType = ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)))

  def rules: RuleSet = new DefaultRuleSet(
    name = "TuplesTransformers Laws",
    parent = None,
    "TuplesTransformers idempotency" -> Prop.forAll { (inp: (Int, (Double, Float), String)) =>
      TuplesTransformersLaws[(Int, (Double, Float), String)].idempotency(inp)
    },
    "TuplesTransformers types checking" -> Prop.forAll { (inp: TestType) =>
      val gr: (Int, Long, String, Boolean, Double, Char, Float, Byte) = inp.toFlatten
      val fr: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNested(gr)
      val ntd: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = inp.toNested
      Prop.passed
    }
  )
}