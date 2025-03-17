# Tuples transformations
This is mini project to transform tuple and its types.

To install
```scala
libraryDependencies += "io.github.mercurievv.minuscles" % "tuples_transformers" % "@TUPLES_TRANSFORMERS_VERSION@"
```

Use import
```scala mdoc
import io.github.mercurievv.minuscles.tuples.transformers.all.*
```
Let's create some input trait

```scala mdoc
val input: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = ((1, 2L), ("3", (true, 0.5D, "6".charAt(0)), (0.7F, 0x8)))
```
Now we will do some transformations: 

#### flattening, nesting
```scala mdoc
val flatten: (Int, Long, String, Boolean, Double, Char, Float, Byte) = input.toFlatten
val flattenToNested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNestedR(flatten)
//flatToNested method works correctly only with flattened tuples, but in such cases it should save some CPU
val nested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = input.toNestedR
```
#### Elements swap
Swapping 2 elements of tuple.
```scala mdoc
val swapped: (Int, Boolean, String, Long, Double, Char, Float, Byte) = flatten.swap(2, 4)
```