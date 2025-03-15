package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import io.github.mercurievv.minuscles.tuples.transformers.all.*
import io.github.mercurievv.minuscles.tuples.transformers.compiletime.swapValues

import scala.util.Random

class TuplesTransformersTests extends Laws {
  // todo extract from class, pass as arguments
  type TestType        = ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)), String)
  type TestTypeFlatten = Flatten[TestType]

  def rules: RuleSet = new DefaultRuleSet(
    name   = "TuplesTransformers Laws",
    parent = None,
    "Flatten <> Nested isomorphism" -> Prop.forAll { (inp: TestType) =>
      MorphismsLaws[TestType, FlattenF, NestedRF](flattenF, nestedRF, nestedRF, flattenF).isomorphism(inp)
    },
    "TuplesTransformers types checking" -> Prop.forAll { (inp: TestType) =>
      val gr: (Int, Long, String, Boolean, Double, Char, Float, Byte, String)                = inp.toFlatten
      val fr: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, (Byte, String))))))))  = flatToNestedR(gr)
      val ntd: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, (Byte, String)))))))) = inp.toNestedR
      Prop.passed
    },
    "TuplesTransformers swap types checking" -> Prop.forAll { (inp: TestType) =>
      val tt                                     = ("1", 2, 3d, 4L, 5f)
      val bb: (String, Long, Double, Int, Float) = tt.swap(2, 4)
      println(bb)
      Prop.passed
    },
    "after swap, tuple contains same elements" -> Prop.forAll { (inp: TestTypeFlatten) =>
      val (n1, n2) = swapValues
      TupleElementsLaws[TestTypeFlatten, Swap[TestTypeFlatten, n1.type, n2.type]](_.swap(n1, n2))
        .containsSameElements(inp)
    },
    "after swap, 2 elements order are different" -> Prop.forAll { (inp: TestTypeFlatten) =>
      val (n1, n2) = swapValues
      TupleElementsLaws[TestTypeFlatten, Swap[TestTypeFlatten, n1.type, n2.type]](_.swap(n1, n2))
        .elementsChanged(inp, 2)
    },
  )
}
