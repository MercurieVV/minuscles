package io.github.mercurievv.minuscles.conversions.deep
import cats.{Functor, ~>}
import cats.syntax.functor._

object DeepConverter {
  def id[A]: DeepConverter[A, A]                 = DeepConverter.lift(identity)
  def lift[A, B](f: A => B): DeepConverter[A, B] = (a: A) => f(a)
//  def liftOpop[H[_[_]], F[_], G[_], A, B](implicit hfg: H[F] => H[G]): DeepConverter[A, B] = (a: A) => hfg(a)
  def liftK[A, B, F[_], G[_]: Functor](fg: F ~> G)(implicit f: A => B): DeepConverter[F[A], G[B]] = (fa: F[A]) =>
    fg(fa).map(f)
}
