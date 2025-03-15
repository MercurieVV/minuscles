package io.github.mercurievv.minuscles.tuples.transformers

import io.github.mercurievv.minuscles.tuples.transformers.all.*
import org.scalacheck.Prop

trait TupleElementsLaws[InpTuple <: Tuple, OutTuple <: Tuple](
    val process: InpTuple => OutTuple
):
  // tuple have same elemnts. But order and structure not guaranteed be preserved
  inline def containsSameElements(in: InpTuple): Prop = {
    val flatten = in.toFlatten
    val out     = process(in)
    out.toList.map(_.hashCode).sum == flatten.toList.map(_.hashCode).sum
  }

  inline def elementsChanged(in: InpTuple, expectedElementsChanged: Int): Prop = {
    val flatten = in.toFlatten
    val out     = process(in)
    out.toList.zipWithIndex.diff(flatten.toList.zipWithIndex).size == expectedElementsChanged
  }

  // this one check that order of element preserved, but structure could be changed
  inline def elementsOrderPreserved(in: InpTuple): Prop = {
    elementsChanged(in, 0)
  }

object TupleElementsLaws:
  def apply[InpTuple <: Tuple, OutTuple <: Tuple](aProcess: InpTuple => OutTuple) =
    new TupleElementsLaws[InpTuple, OutTuple](aProcess) {}
