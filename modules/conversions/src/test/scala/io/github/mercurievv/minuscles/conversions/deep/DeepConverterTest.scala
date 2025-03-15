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

package io.github.mercurievv.minuscles.conversions.deep
import cats.arrow.FunctionK
import cats.implicits._
import cats.~>
import io.github.mercurievv.minuscles.tuples.plens.implicits.Tuple_3_2_PlensOps

object DeepConverterTest {
  import implicits._

  type TestType[F[_]] = Option[Vector[F[(Int, F[String], List[Boolean])]]]
  type InputType      = TestType[Vector] // Option[Vector[Vector[(Int, Vector[String], List[Boolean])]]]
  type OutputType     = TestType[List]   // Option[List[List[(Int, List[String], List[Boolean])]]]
  val vect: InputType = Option(Vector(Vector((5, Vector("String"), List(true)))))
  val lst: OutputType = vect.deepConvert[OutputType]

  assert(lst == Option(List(List((5, List("String"), List(true))))))

  // setup:
  def main(args: Array[String]): Unit = {}

  implicit val vectorToListFK: Vector ~> List = new FunctionK[Vector, List] {
    override def apply[A](fa: Vector[A]): List[A] = fa.toList
  }
  implicit def customTypeMapper[A, B](implicit abc: DeepConverter[A, B]): DeepConverter[Vector[A], List[B]] =
    DeepConverter.liftK(vectorToListFK)
}
