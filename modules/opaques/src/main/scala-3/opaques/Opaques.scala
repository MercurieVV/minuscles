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

package io.github.mercurievv.minuscles.opaques

import scala.language.implicitConversions
import scala.compiletime.{constValue, erasedValue, summonInline}

/** Defines an opaque type wrapper with conversion utilities.
  *
  * Example:
  * {{{
  * val UserName = Opaque.create[String]
  * type UserName = UserName.Opq
  *
  * object UserName extends Opaque[String]
  *
  * val userName    = UserName("MercurieVV")
  * val userNameStr = UserName.unwrap(userName)
  * assert(userNameStr == "MercurieVV")
  * }}}
  */
trait Opaque[Raw]:
  opaque type Opq = Raw

  inline def apply(a: Raw): Opq = a

  inline def unwrap(t: Opq): Raw = t

  object implicits:
    inline implicit def rawToOpq(t: Raw): Opq = t
    inline implicit def opqToRaw(t: Opq): Raw = t

object Opaque:
  def create[Raw] = new Opaque[Raw] {}
