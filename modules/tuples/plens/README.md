# Micro library to modify tuples elements and their types

This project use Monocle

Include in project:
```scala
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "io.github.MercurieVV" % "Repo" % "???"
```

Example:
```scala
import io.github.mercurievv.minuscles.tuples.plens.implicits._
import monocle.syntax.all._
val a = (true, 5, "pep")
a.at[2].modify(_.toString) // (true, "5", "pep")
```