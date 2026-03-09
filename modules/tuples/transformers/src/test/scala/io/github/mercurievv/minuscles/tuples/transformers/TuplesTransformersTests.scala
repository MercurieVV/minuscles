/*
 * Copyright 2025 Viktors Kalinins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.mercurievv.minuscles.tuples.transformers

import org.scalacheck.Prop.*
import org.scalacheck.*
import org.typelevel.discipline.Laws
import io.github.mercurievv.minuscles.tuples.transformers.all.*
import io.github.mercurievv.minuscles.tuples.transformers.compiletime.swapValues

import scala.annotation.nowarn
import scala.compiletime.testing.typeChecks

class TuplesTransformersTests extends Laws {
  // todo extract from class, pass as arguments
  type TestType        = ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)), String)
  type TestTypeFlatten = Flatten[TestType]

  val (n1, n2) = swapValues
  println(s"$n1 $n2")

  @nowarn
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
      (n1 != n2) ==> {
        TupleElementsLaws[TestTypeFlatten, Swap[TestTypeFlatten, n1.type, n2.type]](_.swap(n1, n2))
          .containsSameElements(inp)
      }
    },
    "after swap, 2 elements order are different" -> Prop.forAll { (inp: TestTypeFlatten) =>
      (inp.apply(n1 - 1) != inp.apply(n2 - 1)) ==> {
        TupleElementsLaws[TestTypeFlatten, Swap[TestTypeFlatten, n1.type, n2.type]](_.swap(n1, n2))
          .elementsChanged(inp, 2)
      }
    },
    "map reorder - type checking" -> Prop.forAll { (inp: (String, Int, Double)) =>
      val result: (Int, Double, String) = reorder[(String, Int, Double), (Int, Double, String)](inp)
      result == (inp._2, inp._3, inp._1)
    },
    "map reorder - contains same elements" -> Prop.forAll { (inp: (String, Int, Double)) =>
      TupleElementsLaws[(String, Int, Double), (Int, Double, String)](
        reorder[(String, Int, Double), (Int, Double, String)](_)
      ).containsSameElements(inp)
    },
    "mapTo reorder - type checking" -> Prop.forAll { (inp: (String, Int, Double)) =>
      val result: (Int, Double, String) = inp.reorderTo[(Int, Double, String)]
      result == (inp._2, inp._3, inp._1)
    },
    "map reorder - identity permutation" -> Prop.forAll { (inp: (String, Int, Double)) =>
      val result: (String, Int, Double) = reorder[(String, Int, Double), (String, Int, Double)](inp)
      result == inp
    },
    "map reorder - reverse permutation" -> Prop.forAll { (inp: (String, Int, Double)) =>
      val result: (Double, Int, String) = reorder[(String, Int, Double), (Double, Int, String)](inp)
      result == (inp._3, inp._2, inp._1)
    },
    "map rejects duplicate types in source" -> Prop(
      !typeChecks("""
        import io.github.mercurievv.minuscles.tuples.transformers.all.*
        val bad: (String, Int, String) = ("a", 1, "b")
        reorder[(String, Int, String), (Int, String)](bad)
      """)
    ),
    "fromFlatten round-trips with flatten" -> Prop.forAll { (inp: TestType) =>
      fromFlatten[TestType](inp.toFlatten) == inp
    },
    "fromNestedR round-trips with toNestedR" -> Prop.forAll { (inp: TestType) =>
      fromNestedR[TestType](inp.toNestedR) == inp
    },
    "fromFlatten on already-flat tuple is identity" -> Prop.forAll { (inp: TestTypeFlatten) =>
      fromFlatten[TestTypeFlatten](inp.toFlatten) == inp
    },
    "map accepts distinct types in source" -> Prop(
      typeChecks("""
        import io.github.mercurievv.minuscles.tuples.transformers.all.*
        val ok: (String, Int, Double) = ("a", 1, 2.0)
        reorder[(String, Int, Double), (Int, String)](ok)
      """)
    ),
  )
}
