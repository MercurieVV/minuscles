package io.github.mercurievv.minuscles.fieldsnames.derivation

import io.github.mercurievv.minuscles.fieldsnames.FieldsNames
import io.github.mercurievv.minuscles.fieldsnames.derivation.semiauto.FieldNamesDerivation

import scala.deriving.Mirror

object semiauto {
  import magnolia1.*

  object FieldNamesDerivation extends AutoDerivation[FieldsNames]:
    override def join[T](ctx: CaseClass[FieldsNames, T]): FieldsNames[T] = t =>
      ctx.construct(v => v.typeclass.withFieldsNames(t + v.label))

    override def split[T](sealedTrait: SealedTrait[FieldNamesDerivation.Typeclass, T]): FieldNamesDerivation.Typeclass[T] = ???

    inline given fieldsNames[A](using Mirror.Of[A]): FieldsNames[A] = derived
}
