package com.github.mercurievv.minuscles.tuples.plens.generator

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

object TuplesPLensGenerator {
  def createOrOverrideFile(path: String, content: String): Unit = {
    val filePath = Paths.get(path)
    if (!Files.exists(filePath.getParent)) {
      Files.createDirectories(filePath.getParent)
    }
    Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  def gen(maxAirity: Int): String = {
    val airityAndEldementNr = for {
      airity <- Range(1, maxAirity)
      elNr <- Range(1, airity + 1)
    } yield (airity, elNr)

    val data = airityAndEldementNr.map{case (airity, elNr) => {
      val As = Range(1, airity + 1).map(_.toString).map("A" + _).mkString(", ")
      val ABs = Range(1, airity + 1).map(v =>
        if(v == elNr)
          "B"
        else
          "A" + v.toString
      ).mkString(", ")
      (airity, elNr, As, ABs)
    }}

    val defsApplied = data
      .map((defsAppliedGenerator _).tupled).mkString("\n")
    val defsPlens = data
      .map((defsPlensGenerator _).tupled).mkString("\n")
    val ops = data
      .map((opsGenerator _).tupled).mkString

    s"""
      |package com.github.mercurievv.minuscles.tuples.plens
      |
      |import monocle.syntax.AppliedPLens
      |import monocle.PLens
      |
      |object implicits {
      |$ops
      |
      |$defsPlens
      |
      |$defsApplied
      |}
      |""".stripMargin
  }

  def defsAppliedGenerator(airity: Int, elNr: Int, As: String, ABs: String): String = {
    s"private def tuple_${airity}_${elNr}_plensApplied[$As, B](tuple: Tuple$airity[$As]): AppliedPLens[Tuple$airity[$As], Tuple$airity[$ABs], A$elNr, B] = AppliedPLens.apply(tuple, tuple_${airity}_${elNr}_plens)"
  }
  def defsPlensGenerator(airity: Int, elNr: Int, As: String, ABs: String): String = {
    s"private def tuple_${airity}_${elNr}_plens[$As, B] = PLens.apply[Tuple$airity[$As], Tuple$airity[$ABs], A$elNr, B](s => s._$elNr)(b => s => s.copy(_$elNr = b))"
  }
  def opsGenerator(airity: Int, elNr: Int, As: String, ABs: String): String = {
    s"""
      |implicit class Tuple_${airity}_${elNr}_PlensOps[$As, B](tuple: Tuple$airity[$As]) {
      |  def at[I <: $elNr]: AppliedPLens[Tuple$airity[$As], Tuple$airity[$ABs], A$elNr, B] = tuple_${airity}_${elNr}_plensApplied(tuple)
      |}""".stripMargin
  }
}
