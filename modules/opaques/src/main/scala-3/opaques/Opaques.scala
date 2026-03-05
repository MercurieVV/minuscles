package io.github.mercurievv.minuscles.opaques

import scala.compiletime.{constValue, erasedValue, summonInline}

trait Opaque[Raw]:
  opaque type Opq = Raw

  inline def apply(a: Raw): Opq = a

  inline def unwrap(t: Opq): Raw = t

  object implicits:
    inline implicit def rawToOpq(t: Raw): Opq = t
    inline implicit def opqToRaw(t: Opq): Raw = t

object Opaque:
  def create[Raw] = new Opaque[Raw]{}

