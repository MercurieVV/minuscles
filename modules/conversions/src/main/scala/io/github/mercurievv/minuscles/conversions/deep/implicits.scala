package io.github.mercurievv.minuscles.conversions.deep
import cats.Functor
import cats.arrow.{Arrow, ArrowChoice, CommutativeArrow, FunctionK}

object implicits {
  // implicit def convertId[A]: DeepConverter[A, A] = (a1: A) => a1

  object others {
    implicit def functorId[F[_]: Functor, A, B](implicit dcab: DeepConverter[A, B]): DeepConverter[F[A], F[B]] =
      DeepConverter.liftK(FunctionK.id[F])
  }
  implicit class DeepConverterOpsI[A, B](dc: DeepConverter[A, B]) {
    def deepConvert: DeepConverter[A, B] = dc
  }
  implicit class DeepConverterOps[A](a: A) {
    def deepConvert[B](implicit deepConverter: DeepConverter[A, B]): B = deepConverter.apply(a)
  }
}
