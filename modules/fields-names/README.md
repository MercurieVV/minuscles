# FieldsNames

FieldsNames is scala (2.13, 3) nano library. It's typeclass and derivator, which generate instance of class, where its fields values are strings, containing fields names. Its derivator based on magnolia.

## Installation

```sbt
libraryDependencies += "com.softwaremill.magnolia1_2" %% "magnolia" % "0.1.0"
```

## Use case

Inital purpose for this, was - use for logging. You can have your logic based on case classes fields. Using this typeclass and some other tools, you can generate string representation of that logic or its part.

## Usage

```scala
import io.github.mercurievv.minuscles.fieldsnames._

case class Pep[A](ololo: A, ddd: Int => A)

val derived = derivation.semiauto.FieldNamesDerivation.fieldsNames[Pep[String]]
val inst    = derived.withFieldsNames("")
println(inst.ololo) //will print "ololo"
println(inst.ddd(7)) // will print "ddd(7)"

```

You can create your own typeclass instances. For example:

```scala
implicit def funcFieldName[A]: FieldNamesDerivation[A => String] = (fieldName: String) =>
  (inpValue: A) => s"$fieldName executed with value $inpValue"
}
println(inst.ddd(7)) // will print "ddd executed with value 7"

```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)