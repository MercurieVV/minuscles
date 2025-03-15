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
