# Tuples transformations
This is mini project to transform tuple and its types.

It's Scala 3 only.

To install
```scala
libraryDependencies += "io.github.mercurievv.minuscles" % "tuples-transformers" % "@TUPLES_TRANSFORMERS_VERSION@"
```

Use import
```scala mdoc
import io.github.mercurievv.minuscles.tuples.transformers.all.*
```

Let's create some input data:
```scala mdoc
val input: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = ((1, 2L), ("3", (true, 0.5D, "6".charAt(0)), (0.7F, 0x8)))
```
Now we will do some transformations: 

#### Flattening, Nesting
```scala mdoc
val flatten: (Int, Long, String, Boolean, Double, Char, Float, Byte) = input.toFlatten
val flattenToNested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = flatToNestedR(flatten)
//flatToNested method works correctly only with flattened tuples, but in such cases it should save some CPU
val nested: (Int, (Long, (String, (Boolean, (Double, (Char, (Float, Byte))))))) = input.toNestedR
```
#### Two Elements Swap
Swapping 2 elements of tuple.
```scala mdoc
val swapped: (Int, Boolean, String, Long, Double, Char, Float, Byte) = flatten.swap(2, 4)
```

#### Unflatten
Reconstruct the original nested structure from a flat tuple.
```scala mdoc
val fromFlattenV: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = fromFlatten[((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)))](flatten)
```

#### From Nested-Right
Reconstruct the original structure from a nested-right tuple.
```scala mdoc
val fromNested: ((Int, Long), (String, (Boolean, Double, Char), (Float, Byte))) = fromNestedR[((Int, Long), (String, (Boolean, Double, Char), (Float, Byte)))](nested)
```

#### Reorder by Type
Reorder tuple elements by specifying the desired output type. All element types must be unique.
```scala mdoc
val t = ("hello", 42, 3.14)
val reordered: (Int, Double, String) = reorder[(String, Int, Double), (Int, Double, String)](t)
val reorderedExt: (Int, Double, String) = t.reorderTo[(Int, Double, String)]
```

Duplicate types in the source are rejected at compile time:
```scala mdoc:fail
val bad: (String, Int, String) = ("a", 1, "b")
reorder[(String, Int, String), (Int, String)](bad)
```

#### Distinct types constraint
`IsDistinct[T]` is a typeclass witness that all element types in `T` are unique.
It is used automatically by `reorder`/`reorderTo`, but can also be used as a constraint in your own code.

Require a distinct tuple at a call site:
```scala mdoc
val distinct = t.requireDistinct  // compiles — (String, Int, Double) has unique types
```

```scala mdoc:fail
val bad2: (String, Int, String) = ("a", 1, "b")
bad2.requireDistinct
```

Use as a constraint in your own function:
```scala mdoc:compile-only
def processDistinct[T <: Tuple](t: T)(using IsDistinct[T]): String = t.toString
```