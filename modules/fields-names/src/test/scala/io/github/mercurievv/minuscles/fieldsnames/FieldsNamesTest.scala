package io.github.mercurievv.minuscles.fieldsnames

import munit.FunSuite

class FieldsNamesTest extends FunSuite {
  case class Pep[A](ololo: A, ddd: Int => A)
  test("hello") {
    val derived = derivation.semiauto.FieldNamesDerivation.fieldsNames[Pep[String]]
    val inst    = derived.withFieldsNames("")
    assert(inst.ololo == "ololo")
    assert(inst.ddd(7) == "ddd(7)")
  }
}
