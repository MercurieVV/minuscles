package io.github.mercurievv.minuscles.conversions

package object deep {
  type DeepConverter[A, B]                 = Function1[A, B]
  type DeepConverterK[X[_[_]], F[_], G[_]] = DeepConverter[X[F], X[G]]
}
