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
val userNameStr = UserName.unwrap(userName)
assert(userNameStr == "MercurieVV")
```