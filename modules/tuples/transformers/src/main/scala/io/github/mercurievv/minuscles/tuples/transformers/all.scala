package io.github.mercurievv.minuscles.tuples.transformers

import scala.Tuple.{Append, Concat, Drop, Elem, Take}
import scala.compiletime.ops.int.*

object all:
  type Flatten[T <: Tuple] <: Tuple = T match {
    case EmptyTuple => EmptyTuple
    case h *: tail => h match {
      case Tuple => Flatten[h] `Concat` Flatten[tail]
      case _ => h *: Flatten[tail]
    }
  }

  type FlatToNested[T <: Tuple] <: Tuple = T match {
    case EmptyTuple => EmptyTuple
    case h *: EmptyTuple => h *: EmptyTuple
    case h *: h2 *: EmptyTuple => (h, h2)
    case h *: tail => (h, FlatToNested[tail])
  }


  extension [T <: Tuple] (t: T)
    inline def toFlatten : Flatten[T] = flatten(t)
    inline def flattenToNested : FlatToNested[T] = flatToNested(t)
    inline def toNested : FlatToNested[Flatten[T]] = nested(t)
  extension[T <: NonEmptyTuple] (inline t: T)
    transparent inline def swap[N1 <: Int, N2 <: Int](inline n1: N1, inline n2 : N2) : Any = swapElements(t)(n1, n2)


  inline def flatten[T <: Tuple](t: T): Flatten[T] =
    flattenRuntime(t).asInstanceOf[Flatten[T]]
  inline def flattenRuntime[T <: Tuple](t: T): Tuple =
    inline t match
      case EmptyTuple => EmptyTuple
      case ts: *:[*:[h, ht], t] => flatten(ts.head.asInstanceOf[h *: ht]) ++ flatten(ts.tail)
      case ts: (h *: t) => ts.head.asInstanceOf[h] *: flatten(ts.tail)

  inline def imin(n1: Int, n2: Int): Min[n1.type, n2.type] = n1.min(n2).asInstanceOf
  inline def imax(n1: Int, n2: Int): Max[n1.type, n2.type] = n1.max(n2).asInstanceOf

  type Swap[T <: NonEmptyTuple, MINN <: Int, MAXN <: Int] =
    Concat[
        Append[
          Concat[
            Append[Take[T, MINN], Elem[T, MAXN]],
            Drop[Take[T, MAXN], S[MINN]]
          ],
          Elem[T, MINN]
      ],
      Drop[T, S[MAXN]]
    ]

  inline transparent def swapElements[T <: NonEmptyTuple, N1 <: Int, N2 <: Int]( t: T)(n1: N1, n2: N2): Any =
    val nMin: Min[n1.type , n2.type ] = n1.min(n2).asInstanceOf
    val nMax: Max[n1.type , n2.type] = n1.max(n2).asInstanceOf
    val nMinm1: Min[n1.type , n2.type] - 1 = (nMin - 1).asInstanceOf
    val nMaxm1: Max[n1.type , n2.type] - 1 = (nMax - 1).asInstanceOf

    runtimeSwap[T, nMin.type, nMax.type , nMinm1.type, nMaxm1.type](t)(nMin, nMax, nMinm1, nMaxm1)//.asInstanceOf[Swap[T, nMinm1.type , nMaxm1.type]]

  transparent inline def runtimeSwap[T <: NonEmptyTuple, NMin <: Int, NMax <: Int, NMinm1 <: Int, NMaxm1 <: Int](t: T)(
    nMin: NMin,nMax: NMax,nMinm1: NMinm1, nMaxm1: NMaxm1) : Tuple =

    val tp1 = t.take(nMinm1)
    val el1 = t.apply(nMaxm1)
    val tp2 = t.take(nMaxm1).drop(nMin)
    val el2 = t.apply(nMinm1)
    val tp3 = t.drop(nMax)
    (((tp1 :* el1) ++ tp2 :* el2) ++ tp3).asInstanceOf[Swap[T, nMinm1.type , nMaxm1.type]]

  inline def flatToNested[T <: Tuple](t: T): FlatToNested[T] =
    flatToNestedRuntime(t).asInstanceOf[FlatToNested[T]]
  def flatToNestedRuntime[T <: Tuple](t: T): Tuple =
    t match
      case EmptyTuple => EmptyTuple
      case h *: EmptyTuple => h *: EmptyTuple
      case h *: h2 *: EmptyTuple => (h, h2)
      case h *: tail => (h, flatToNested(tail))

  inline def nested[T <: Tuple](t: T): FlatToNested[Flatten[T]] = {
    flatToNested(flatten(t))
  }

