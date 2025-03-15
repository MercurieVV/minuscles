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
