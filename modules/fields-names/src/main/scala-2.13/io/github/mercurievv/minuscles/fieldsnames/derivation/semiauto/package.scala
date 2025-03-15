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

package io.github.mercurievv.minuscles.fieldsnames.derivation

import io.github.mercurievv.minuscles.fieldsnames.FieldsNames
import magnolia1._

package object semiauto {

  import language.experimental.macros

  object FieldNamesDerivation {
    type Typeclass[T] = FieldsNames[T]

    def join[T](ctx: CaseClass[Typeclass, T]): FieldsNames[T] = { (t: String) =>
      ctx.construct(v => v.typeclass.withFieldsNames(t + v.label))
    }

    /*
    def split[T](ctx: SealedTrait[FieldsNames, T]): FieldsNames[T] = ???
/*      (value: T) =>
        ctx.split(value) { sub =>
          sub.typeclass.withFieldsNames(sub.cast(value))
        }*/
     */

    implicit def gen[T]: FieldsNames[T]         = macro Magnolia.gen[T]
    implicit def fieldsNames[T]: FieldsNames[T] = macro Magnolia.gen[T]
  }

}
