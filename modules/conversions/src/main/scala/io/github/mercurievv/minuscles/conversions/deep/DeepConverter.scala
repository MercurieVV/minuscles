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
import cats.implicits.toComposeOps
import cats.{Functor, ~>}
import cats.syntax.functor.*

object DeepConverter {
  def id[A]: DeepConverter[A, A]                 = DeepConverter.lift(identity)
  def lift[A, B](f: A => B): DeepConverter[A, B] = (a: A) => f(a)
  def liftK[A, B, F[_], G[_]: Functor](fg: F ~> G)(implicit f: A => B): DeepConverter[F[A], G[B]] = (fa: F[A]) =>
    fg(fa).map(f)
}
