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

    implicit def gen[T]: FieldsNames[T] = macro Magnolia.gen[T]
  }

  def fieldsNames[T]: FieldsNames[T] = macro Magnolia.gen[T]
}
