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

import scala.deriving.Mirror

object semiauto {
  import magnolia1.*

  object FieldNamesDerivation extends AutoDerivation[FieldsNames]:
    override def join[T](ctx: CaseClass[FieldsNames, T]): FieldsNames[T] = t =>
      ctx.construct(v => v.typeclass.withFieldsNames(t + v.label))

    override def split[T](
        sealedTrait: SealedTrait[FieldNamesDerivation.Typeclass, T]
    ): FieldNamesDerivation.Typeclass[T] = ???

    inline given fieldsNames[A](using Mirror.Of[A]): FieldsNames[A] = derived
}
