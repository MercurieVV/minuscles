package io.github.mercurievv.minuscles.fieldsnames

object FieldsNames {
  implicit def FieldsNamesString: FieldsNames[String] = (fieldName: String) => fieldName
  implicit def FieldsNamesFunction1[A]: FieldsNames[A => String] = (fieldName: String) =>
    (inpValue: A) => s"$fieldName($inpValue)"
}
trait FieldsNames[T] {
  def withFieldsNames(fieldInfo: String): T
}
