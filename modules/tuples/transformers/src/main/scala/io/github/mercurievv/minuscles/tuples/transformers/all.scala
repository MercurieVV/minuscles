package io.github.mercurievv.minuscles.tuples.transformers

import scala.Tuple.Concat
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
  
  
  inline def flatten[T <: Tuple](t: T): Flatten[T] =
    flattenRuntime(t).asInstanceOf[Flatten[T]]
  inline def flattenRuntime[T <: Tuple](t: T): Tuple =
    inline t match
      case EmptyTuple => EmptyTuple
      case ts: *:[*:[h, ht], t] => flatten(ts.head.asInstanceOf[h *: ht]) ++ flatten(ts.tail)
      case ts: (h *: t) => ts.head.asInstanceOf[h] *: flatten(ts.tail)
  
  
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
