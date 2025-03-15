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
import io.github.mercurievv.minuscles.tuples.plens.implicits._
import cats.Functor
import cats.arrow.{Arrow, ArrowChoice, CommutativeArrow, FunctionK}
import cats.implicits.toComposeOps

object implicits {
  // implicit def convertId[A]: DeepConverter[A, A] = (a1: A) => a1

  // tuple2
  implicit def tuple21DeepConverter[A1, A2, B1](implicit dc: DeepConverter[A1, B1]): DeepConverter[(A1, A2), (B1, A2)] =
    DeepConverter.lift(_.mod(1)(dc))
  implicit def tuple22DeepConverter[A1, A2, B2](implicit dc: DeepConverter[A2, B2]): DeepConverter[(A1, A2), (A1, B2)] =
    DeepConverter.lift(_.mod(2)(dc))

  // tuple3
  implicit def tuple31DeepConverter[A1, A2, A3, B1](implicit
      dc: DeepConverter[A1, B1]
  ): DeepConverter[(A1, A2, A3), (B1, A2, A3)] = DeepConverter.lift(_.mod(1)(dc))
  implicit def tuple32DeepConverter[A1, A2, A3, B2](implicit
      dc: DeepConverter[A2, B2]
  ): DeepConverter[(A1, A2, A3), (A1, B2, A3)] = DeepConverter.lift(_.mod(2)(dc))
  implicit def tuple33DeepConverter[A1, A2, A3, B3](implicit
      dc: DeepConverter[A3, B3]
  ): DeepConverter[(A1, A2, A3), (A1, A2, B3)] = DeepConverter.lift(_.mod(3)(dc))

  implicit def functorIdentity[F[_]: Functor, A, B](implicit dcab: DeepConverter[A, B]): DeepConverter[F[A], F[B]] =
    DeepConverter.liftK(FunctionK.id[F])

  implicit class DeepConverterOpsI[A, B](dc: DeepConverter[A, B]) {
    def deepConvert: DeepConverter[A, B] = dc
  }

  implicit class DeepConverterOps[A](a: A) {
    def deepConvert[B](implicit deepConverter: DeepConverter[A, B]): B = deepConverter.apply(a)
  }
}
