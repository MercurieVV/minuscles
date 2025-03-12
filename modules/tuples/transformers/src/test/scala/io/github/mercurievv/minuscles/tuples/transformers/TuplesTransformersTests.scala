package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import io.github.mercurievv.minuscles.tuples.transformers.all.*
import io.github.mercurievv.minuscles.tuples.transformers.compiletime.swapValues

import scala.util.Random

class TuplesTransformersTests extends Laws {
  type TestType = ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)), String)
  type TestType2 = ((Int, Long), (Char, (Boolean, Double, Char), (Float, Byte)), Long)

  def rules: RuleSet = new DefaultRuleSet(
    name = "TuplesTransformers Laws",
    parent = None,
    "TuplesTransformers idempotency" -> Prop.forAll { (inp: (Int, (Double, Float), String)) =>
      TuplesTransformersLaws[(Int, (Double, Float), String)].idempotency(inp)
    },
    "TuplesTransformers types checking" -> Prop.forAll { (inp: TestType) =>
      val gr: (Int, Long, String, Boolean, Double, Char, Float, Byte, String) = inp.toFlatten
      val fr: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, (Byte, String)))))))) = flatToNestedR(gr)
      val ntd: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, (Byte, String)))))))) = inp.toNestedR
      Prop.passed
    },
    "TuplesTransformers swap types checking" -> Prop.forAll { (inp: TestType) =>
      val tt = ("1", 2, 3D, 4L, 5F)
      val bb: (String, Long, Double, Int, Float) = tt.swap(2, 4)
      println(bb)
      Prop.passed
    },
    "after swap, elements are the same" -> Prop.forAll { (inp: TestType) =>
      val (n1, n2) = swapValues
      val flatten = inp.toFlatten
      val out = flatten.swap(n1, n2)
      out.toList.map(_.hashCode).sum == flatten.toList.map(_.hashCode).sum
    },
    "after swap, 2 elements order are different" -> Prop.forAll { (inp: TestType) =>
      val (n1, n2) = swapValues
      val flatten = inp.toFlatten
      val out = flatten.swap(n1, n2)
      out.toList.zipWithIndex.diff(flatten.toList.zipWithIndex).size == 2
    }
  )

}
