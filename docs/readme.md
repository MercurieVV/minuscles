To install my project
```scala
libraryDependencies += "com" % "lib" % "@VERSION@"
```

Use import
```scala mdoc
import io.github.mercurievv.minuscles.tuples.transformers.all.*
```
Let's create some input trait
```scala mdoc
val inp: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = ((7, 10L), ("str", (true, 0.5D, "c".charAt(0)), (0.8F, 0x5)))
```
Now we will do some transformations: flattening, nesting
```scala mdoc
val flatten: (Int, Long, String, Boolean, Double, Char, Float, Byte) = inp.toFlatten
val flattenToNested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNested(flatten)
//flatToNested method works correctly only with flattened tuples, but in such cases it should save some CPU
val nested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = inp.toNested

```
