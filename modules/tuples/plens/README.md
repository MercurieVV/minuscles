# Micro library to modify tuples elements and their types

This project use Monocle

Scala 2.13 and 3 supported.

Include in project:
```scala
libraryDependencies += "io.github.mercurievv" %% "tuples_plens" % "0.1.0"
```

Example:
```scala
import io.github.mercurievv.minuscles.tuples.plens.implicits._
import monocle.syntax.all._

val a = (true, 5, "pep")   // (true,  5 , "pep")
a.at[2].modify(_.toString) // (true, "5", "pep")
```