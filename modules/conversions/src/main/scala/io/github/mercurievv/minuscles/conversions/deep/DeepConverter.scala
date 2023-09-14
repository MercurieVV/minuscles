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
