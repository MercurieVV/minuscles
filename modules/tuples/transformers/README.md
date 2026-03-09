# Micro library to modify tuples structure and their types

Scala only 3 supported.

see docs here: https://mercurievv.github.io/minuscles/tuples-transformers.html

## What's new

### v 0.2.1

* `fromFlatten` — reconstruct nested structure from a flat tuple. Inverse of `toFlatten`. Given the target type `T`, reconstructs the original nesting from a flat tuple.
* `fromNestedR` — reconstruct original structure from a nested-right tuple. Inverse of `toNestedR`. Converts a right-nested tuple back to any target structure `T`.
* `reorder` — reorder elements by type. Reorders tuple elements to match a specified target type. Element types must be unique (enforced at compile time).
* `requireDistinct` — checks that tuple have only unique types.
