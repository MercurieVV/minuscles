package io.github.mercurievv.minuscles.context4log4cats

import cats.effect.IO
import cats.effect.implicits.*
import cats.implicits.*
import org.scalacheck.Gen
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.testing.StructuredTestingLogger
import weaver.*
import weaver.scalacheck.*

import scala.concurrent.duration.DurationInt

object LogContextSpec extends SimpleIOSuite {

  test("Single Gen form") {
    val logger                                                         = StructuredTestingLogger.impl[IO]()
    val contexted: IO[(SelfAwareStructuredLogger[IO], LogContext[IO])] = LogContext.create(logger)

    for {
      (l, ctx) <- contexted
      _        <- l.info("log before context")
      _ <- ctx.context(Map("init context" -> "init context val"))(cl =>
        (cl.add(Map("added to context" -> "added to context val")) *> l.info("log in context")) *> (IO.sleep(3.seconds) *> l.info("new thread")).start
      )
      _ <- l.info("log after context")
      _ <- IO.sleep(5.seconds)
      v <- logger.logged
      _ = println(v)
    } yield (expect(1 == 1))

  }
}
