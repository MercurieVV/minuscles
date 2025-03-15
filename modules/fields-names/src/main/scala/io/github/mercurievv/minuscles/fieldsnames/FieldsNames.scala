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

object FieldsNames {
  implicit def FieldsNamesString: FieldsNames[String] = (fieldName: String) => fieldName
  implicit def FieldsNamesFunction1[A]: FieldsNames[A => String] = (fieldName: String) =>
    (inpValue: A) => s"$fieldName($inpValue)"
}
trait FieldsNames[T] {
  def withFieldsNames(fieldInfo: String): T
}
