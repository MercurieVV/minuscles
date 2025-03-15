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

package io.github.mercurievv.minuscles.tuples.transformers

import cats.arrow.FunctionK
import cats.syntax.functor.*
import cats.syntax.applicative.*
import org.scalacheck.Prop
import org.typelevel.discipline.Laws
import cats.{Applicative, Functor, InjectK, Monad}
import io.github.mercurievv.minuscles.tuples.transformers.all.*

trait MorphismsLaws[A, F[_], G[_]](
    a2f: A => F[A],
    a2g: A => G[A],
    f2g: F[A] => G[A],
    g2f: G[A] => F[A],
) {

  inline def isomorphism(a: A): Prop =
    val af    = a2f(a)
    val aBack = g2f(f2g(af))
    val ag    = a2g(a)
    val bBack = f2g(g2f(ag))
    aBack == af && bBack == ag
}

object MorphismsLaws {
  def apply[A, F[_], G[_]](
      a2f: A => F[A],
      a2g: A => G[A],
      f2g: F[A] => G[A],
      g2f: G[A] => F[A],
  ): MorphismsLaws[A, F, G] =
    new MorphismsLaws[A, F, G](a2f, a2g, f2g, g2f) {}
}
