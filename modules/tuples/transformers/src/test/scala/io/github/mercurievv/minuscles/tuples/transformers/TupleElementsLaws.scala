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
