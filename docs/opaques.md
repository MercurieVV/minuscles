# Opaques
This is mini project to create opaque types, and companion objects.

It's Scala 3 only.

To install
```scala
libraryDependencies += "io.github.mercurievv.minuscles" % "opaques" % "@OPAQUES_VERSION@"
```

Use import
```scala mdoc
import io.github.mercurievv.minuscles.opaques.*
```

Let's create opaque type:
```scala mdoc
val UserName = Opaque.create[String]
type UserName = UserName.Opq
```
Now we will do some mappings:

#### Opaque object creation and getting back
```scala mdoc
val userName = UserName("MercurieVV")
val userNameStr = userName.value
assert(userNameStr == "MercurieVV")
```

## Opaque functor

Create functor to mark input data
```scala mdoc
object Input extends OpaqueFunctor
type Input[a] = Input.Opq[a]
```

Now wrap some value
```scala mdoc
val inputInt: Input[Int] = Input(5)
```
Getting original value
```scala mdoc
val myInt: Int = inputInt.value
```

`Input[Int]` is a distinct type from `Int` — they are not interchangeable:
```scala mdoc:fail
val notAnInt: Int = inputInt //won't compile
```
