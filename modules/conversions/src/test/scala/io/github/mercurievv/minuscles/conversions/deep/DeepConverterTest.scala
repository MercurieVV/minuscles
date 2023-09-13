package io.github.mercurievv.minuscles.conversions.deep
import cats.arrow.FunctionK
import cats.implicits._
import cats.~>
import io.github.mercurievv.minuscles.tuples.plens.implicits.Tuple_3_2_PlensOps

object DeepConverterTest {
//  type TestType[F[_]] = (Int, F[String], List[Boolean])
  type TestType[F[_]] = Option[Vector[F[(Int, F[String], List[Boolean])]]]
  // type TestType[F[_]] = Option[Vector[F[Int]]]

  import implicits._
  import implicits.others._

  implicit val vectToListFK: Vector ~> List = new FunctionK[Vector, List] {
    override def apply[A](fa: Vector[A]): List[A] = fa.toList
  }

  type L[F[_]] = (Int, F[String], List[Boolean])
  implicit def tuple[F[_], G[_]](implicit
      int: DeepConverter[F[String], G[String]]
//for some  reason compilation error:  ): DeepConverter[(Int, F[String], List[Boolean]), (Int, G[String], List[Boolean])] = {
  ): DeepConverter[L[F], L[G]] = {
    DeepConverter.id[(Int, F[String], List[Boolean])] >>> (f => f.mod(2)(int.apply))
  }
  implicit def vectToList[A, B](implicit abc: DeepConverter[A, B]): DeepConverter[Vector[A], List[B]] =
    DeepConverter.liftK(vectToListFK)

//  implicitly[DeepConverter[TestType[Vector], TestType[List]]](tuple)
  val vect: TestType[Vector] = Option(Vector(Vector((5, Vector("String"), List(true)))))
  val lst: TestType[List]    = vect.deepConvert[TestType[List]]

  assert(lst == Option(List(List((5, List("String"), List(true))))))

  def main(args: Array[String]): Unit = {}
}
