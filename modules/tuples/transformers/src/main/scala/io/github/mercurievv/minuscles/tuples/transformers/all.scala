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

import scala.Tuple.{Append, Concat, Drop, Elem, Take}
import scala.compiletime.ops.int.*

object all:
  type Flatten[T <: Tuple] <: Tuple = T match {
    case EmptyTuple => EmptyTuple
    case h *: tail =>
      h match {
        case Tuple => Flatten[h] `Concat` Flatten[tail]
        case _     => h *: Flatten[tail]
      }
  }

  // type not bound to Tuple to use it as abstract F[_]
  type FlattenF[T] <: Tuple = T match {
    case Tuple => Flatten[T]
    case _     => T *: EmptyTuple
  }

  type FlatToNestedR[T <: Tuple] <: Tuple = T match {
    case EmptyTuple            => EmptyTuple
    case h *: EmptyTuple       => h *: EmptyTuple
    case h *: h2 *: EmptyTuple => (h, h2)
    case h *: tail             => (h, FlatToNestedR[tail])
  }

  type NestedR[T <: Tuple] = FlatToNestedR[Flatten[T]]

  type NestedRF[T] = T match {
    case Tuple => NestedR[T]
    case _     => T *: EmptyTuple
  }

  // Reconstructs the original nested structure T from its flat element array
  trait UnflattenFrom[T]:
    def flatSize: Int
    def apply(arr: Array[Object], offset: Int): T

  object UnflattenFrom:
    given scalarUnflatten[T](using scala.util.NotGiven[T <:< Tuple]): UnflattenFrom[T] with
      def flatSize                                  = 1
      def apply(arr: Array[Object], offset: Int): T = arr(offset).asInstanceOf[T]

    given emptyUnflatten: UnflattenFrom[EmptyTuple] with
      def flatSize                                           = 0
      def apply(arr: Array[Object], offset: Int): EmptyTuple = EmptyTuple

    given consUnflatten[H, Tail <: Tuple](using
        head: UnflattenFrom[H],
        tail: UnflattenFrom[Tail],
    ): UnflattenFrom[H *: Tail] with
      def flatSize = head.flatSize + tail.flatSize
      def apply(arr: Array[Object], offset: Int): H *: Tail =
        head(arr, offset) *: tail(arr, offset + head.flatSize)

  private def flattenDynamic(t: Tuple): Tuple = t match
    case EmptyTuple => EmptyTuple
    case h *: tail =>
      h match
        case ht: Tuple => flattenDynamic(ht) ++ flattenDynamic(tail)
        case _         => h *: flattenDynamic(tail)

  def fromFlatten[T <: Tuple](t: Flatten[T])(using uf: UnflattenFrom[T]): T =
    uf(t.toArray, 0)

  def fromNestedR[T <: Tuple](t: NestedR[T])(using uf: UnflattenFrom[T]): T =
    uf(flattenDynamic(t).toArray, 0)

  // Witnesses that all element types in T are distinct
  trait IsDistinct[T <: Tuple]

  object IsDistinct:
    given emptyDistinct: IsDistinct[EmptyTuple] with {}
    given consDistinct[H, Tail <: Tuple](using
        scala.util.NotGiven[TupleIndex[Tail, H]],
        IsDistinct[Tail],
    ): IsDistinct[H *: Tail] with {}

  // Typeclass: index of type E in tuple T (requires unique element types)
  trait TupleIndex[T <: Tuple, E]:
    def index: Int

  object TupleIndex:
    given headIndex[E, Tail <: Tuple]: TupleIndex[E *: Tail, E] with
      def index = 0
    given tailIndex[H, Tail <: Tuple, E](using ti: TupleIndex[Tail, E]): TupleIndex[H *: Tail, E] with
      def index = 1 + ti.index

  // Typeclass: reorder Src tuple into Target tuple layout by matching element types
  trait TupleReorder[Src <: Tuple, Target <: Tuple]:
    def apply(s: Src): Target

  object TupleReorder:
    given emptyReorder[Src <: Tuple]: TupleReorder[Src, EmptyTuple] with
      def apply(tup: Src): EmptyTuple = EmptyTuple
    given consReorder[Src <: Tuple, H, Tail <: Tuple](using
        idx: TupleIndex[Src, H],
        rest: TupleReorder[Src, Tail],
    ): TupleReorder[Src, H *: Tail] with
      def apply(tup: Src): H *: Tail =
        val arr = tup.toArray
        arr(idx.index).asInstanceOf[H] *: rest.apply(tup)

  def reorder[Src <: Tuple, Target <: Tuple](tup: Src)(using
      r: TupleReorder[Src, Target],
      d: IsDistinct[Src],
  ): Target = r.apply(tup)

  extension [T <: Tuple](t: T)
    inline def toFlatten: Flatten[T]                                                           = flatten(t)
    inline def flattenToNestedR: FlatToNestedR[T]                                              = flatToNestedR(t)
    inline def toNestedR: NestedR[T]                                                           = nestedR(t)
    def reorderTo[Target <: Tuple](using r: TupleReorder[T, Target], d: IsDistinct[T]): Target = reorder(t)(using r, d)
    def requireDistinct(using IsDistinct[T]): T                                                = t

  extension [T <: NonEmptyTuple](inline t: T)
    transparent inline def swap[N1 <: Int, N2 <: Int](inline n1: N1, inline n2: N2): Any = swapElements(t)(n1, n2)

  inline def flattenF[T](t: T): FlattenF[T] = {
    t match
      case tpl: Tuple => flatten(tpl)
      case _          => t *: EmptyTuple
  }.asInstanceOf

  inline def flatten[T <: Tuple](t: T): Flatten[T] =
    flattenRuntime(t).asInstanceOf
  inline def flattenRuntime[T <: Tuple](t: T): Tuple =
    inline t match
      case EmptyTuple           => EmptyTuple
      case ts: *:[*:[h, ht], t] => flatten(ts.head.asInstanceOf[h *: ht]) ++ flatten(ts.tail)
      case ts: (h *: t)         => ts.head.asInstanceOf[h] *: flatten(ts.tail)

  inline def imin(n1: Int, n2: Int): Min[n1.type, n2.type] = n1.min(n2).asInstanceOf
  inline def imax(n1: Int, n2: Int): Max[n1.type, n2.type] = n1.max(n2).asInstanceOf

  type Swap[T <: NonEmptyTuple, N1 <: Int, N2 <: Int] = SwapMM[T, Min[N1, N2] - 1, Max[N1, N2] - 1]
  type SwapMM[T <: NonEmptyTuple, MINN <: Int, MAXN <: Int] =
    Concat[
      Append[
        Concat[
          Append[Take[T, MINN], Elem[T, MAXN]],
          Drop[Take[T, MAXN], S[MINN]],
        ],
        Elem[T, MINN],
      ],
      Drop[T, S[MAXN]],
    ]

  inline transparent def swapElements[T <: NonEmptyTuple, N1 <: Int, N2 <: Int](t: T)(n1: N1, n2: N2): Any =
    val nMin: Min[n1.type, n2.type]       = n1.min(n2).asInstanceOf
    val nMax: Max[n1.type, n2.type]       = n1.max(n2).asInstanceOf
    val nMinm1: Min[n1.type, n2.type] - 1 = (nMin - 1).asInstanceOf
    val nMaxm1: Max[n1.type, n2.type] - 1 = (nMax - 1).asInstanceOf

    runtimeSwap[T, nMin.type, nMax.type, nMinm1.type, nMaxm1.type](t)(nMin, nMax, nMinm1, nMaxm1)
      .asInstanceOf[Swap[T, n1.type, n2.type]]

  transparent inline def runtimeSwap[T <: NonEmptyTuple, NMin <: Int, NMax <: Int, NMinm1 <: Int, NMaxm1 <: Int](
      t: T
  )(nMin: NMin, nMax: NMax, nMinm1: NMinm1, nMaxm1: NMaxm1): Tuple =

    val tp1 = t.take(nMinm1)
    val el1 = t.apply(nMaxm1)
    val tp2 = t.take(nMaxm1).drop(nMin)
    val el2 = t.apply(nMinm1)
    val tp3 = t.drop(nMax)
    (((tp1 :* el1) ++ tp2 :* el2) ++ tp3).asInstanceOf[SwapMM[T, nMinm1.type, nMaxm1.type]]

  inline def flatToNestedR[T <: Tuple](t: T): FlatToNestedR[T] =
    flatToNestedRuntime(t).asInstanceOf
  def flatToNestedRuntime[T <: Tuple](t: T): Tuple =
    t match
      case EmptyTuple            => EmptyTuple
      case h *: EmptyTuple       => h *: EmptyTuple
      case h *: h2 *: EmptyTuple => (h, h2)
      case h *: tail             => (h, flatToNestedR(tail))

  inline def nestedR[T <: Tuple](t: T): NestedR[T] = {
    flatToNestedR(flatten(t))
  }

  inline def nestedRF[T](t: T): NestedRF[T] = {
    t match
      case tpl: Tuple => nestedR(tpl)
      case _          => t *: EmptyTuple
  }.asInstanceOf
