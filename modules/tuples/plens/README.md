# Micro library to modify tuples elements and their types

This project use Monocle

Scala 2.13 and 3 supported.

Include in project:
```scala
libraryDependencies += "io.github.mercurievv.minuscles" %% "tuples_plens" % "0.1.1"
```

Example:
```scala
import io.github.mercurievv.minuscles.tuples.plens.implicits._
import monocle.syntax.all._

val a = (true, 5, "pep")           // (true,  5 , "pep")
val b = a.at(2).modify(_.toString) // (true, "5", "pep")
val c = a.mod(2)(_.toString)       // (true, "5", "pep")
```