package io.github.mercurievv.minuscles.tuples.transformers

import munit.DisciplineSuite
import cats.effect.IO

class MorphismsLawsTests extends DisciplineSuite {
  checkAll("Tuples Transformers Laws", new TuplesTransformersTests().rules)
}
