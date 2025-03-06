# Micro library to modify tuples structure and their types

Scala 3 supported.

Include in project:
```scala
libraryDependencies += "io.github.mercurievv.minuscles" %% "tuples_transformers" % "0.1.0"
```

Example:
```scala
import io.github.mercurievv.minuscles.tuples.transformers.all.*

val inp: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = ??? //don't care about value, but its working

//following tranformations:
val flatten: (Int, Long, String, Boolean, Double, Char, Float, Byte) = inp.toFlatten
val flattenToNested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNested(gr) 
//flatToNested method works correctly only with flattened tuples, but in such cases it should save some CPU
val nested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = inp.toNested

```