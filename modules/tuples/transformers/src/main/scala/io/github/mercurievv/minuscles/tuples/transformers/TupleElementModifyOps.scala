package io.github.mercurievv.minuscles.tuples.transformers

import scala.Tuple.{Concat, Drop, Elem, Take}
import scala.compiletime.erasedValue
import scala.compiletime.ops.int.*

type Flatten[T <: Tuple] <: Tuple = T match {
  case EmptyTuple => EmptyTuple
  case h *: tail => h match {
    case Tuple => Flatten[h] `Concat` Flatten[tail]
    case _ => h *: Flatten[tail]
  }
}

type FlatToNested[T <: Tuple] <: Tuple = T match {
  case h *: h2 *: EmptyTuple => (h, h2)
  case EmptyTuple => EmptyTuple
  case h *: tail => (h, FlatToNested[tail])
}


extension [T <: Tuple] (t: T) 
  inline def tFlatten : Flatten[T] = flatten(t)


inline def flatten[T <: Tuple](t: T): Flatten[T] =
  flattenRuntime(t).asInstanceOf[Flatten[T]]
inline def flattenRuntime[T <: Tuple](t: T): Tuple =
  inline t match
    case EmptyTuple => EmptyTuple
    case ts: *:[*:[h, ht], t] => flatten(ts.head.asInstanceOf[h *: ht]) ++ flatten(ts.tail)
    case ts: (h *: t) => ts.head.asInstanceOf[h] *: flatten(ts.tail)


inline def flatToNested[T <: Tuple](t: T): FlatToNested[T] = ???
inline def nested[T <: Tuple](t: T): FlatToNested[Flatten[T]] = ???

type Test = ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)))

val test: Test = ???
val gr: (Int, Long, String, Boolean, Double, Char, Float, Byte) = flatten[Test](???)
val fr: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNested(gr)
val ntd: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = nested(test)
