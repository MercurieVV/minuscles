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

import scala.util.Random

object compiletime:

  import scala.quoted.*

  inline def swapValues: (Int, Int) = ${ swapValuesCode }

  def swapValuesCode(using Quotes): Expr[(Int, Int)] = {
    val n1 = 1 + (Random.nextInt() % 8).abs
    val n2 = 1 + (Random.nextInt() % 8).abs
    val n2f = if (n1 == n2) { (n2 + 1) % 8 }
    else
      n2
    Expr(
      (
        n1,
        n2f,
      )
    )
  }
