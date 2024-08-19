package io.github.mercurievv.minuscles.context4log4cats

import cats.Monad
import cats.effect.kernel.Ref
import cats.effect.kernel.Ref
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxApplyOps, catsSyntaxFlatMapOps, toFlatMapOps, toFunctorOps}
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}

trait LogContext[F[_]] {
  def context[A](ctxValues: Map[String, String])(ctx: ContextUpdater[F] => F[A]): F[A]
}

trait ContextUpdater[F[_]] {
  def add(add: Map[String, String]): F[Unit]
}

object LogContext {
  def create[F[_]: Ref.Make: Monad](
      logger: SelfAwareStructuredLogger[F]
  ): F[(SelfAwareStructuredLogger[F], LogContext[F])] = {
    Ref
      .of[F, Map[String, String]](Map.empty[String, String])
      .map(ref => forRef(ref, logger))
  }

  def forRef[F[_]: Monad](
      ref: Ref[F, Map[String, String]],
      logger: SelfAwareStructuredLogger[F],
  ): (SelfAwareStructuredLogger[F], LogContext[F]) = {

    val context = new LogContext[F] {
      override def context[A](ctxValues: Map[String, String])(ctx: ContextUpdater[F] => F[A]): F[A] = {
        ref
          .set(ctxValues)
          .as({
            val cu: ContextUpdater[F] = (add: Map[String, String]) => {
              ref.update(m => m ++ add)
            }
            cu
          })
          .flatMap(ctx.apply)
          .productL(ref.set(Map.empty))
      }
    }

    val mLogger = new ModifiedContextSelfAwareStructuredLogger[F](logger)(v => ref.get.map(v ++ _))
    (mLogger, context)
  }

  class ModifiedContextSelfAwareStructuredLogger[F[_]: Monad](sl: SelfAwareStructuredLogger[F])(
      modify: Map[String, String] => F[Map[String, String]]
  ) extends SelfAwareStructuredLogger[F] {
    private def defaultCtx                 = modify(Map.empty)
    def error(message: => String): F[Unit] = defaultCtx.flatMap(sl.error(_)(message))
    def warn(message: => String): F[Unit]  = defaultCtx.flatMap(sl.warn(_)(message))
    def info(message: => String): F[Unit]  = defaultCtx.flatMap(sl.info(_)(message))
    def debug(message: => String): F[Unit] = defaultCtx.flatMap(sl.debug(_)(message))
    def trace(message: => String): F[Unit] = defaultCtx.flatMap(sl.trace(_)(message))
    def trace(ctx: Map[String, String])(msg: => String): F[Unit] =
      modify(ctx).flatMap(sl.trace(_)(msg))
    def debug(ctx: Map[String, String])(msg: => String): F[Unit] =
      modify(ctx).flatMap(sl.debug(_)(msg))
    def info(ctx: Map[String, String])(msg: => String): F[Unit] =
      modify(ctx).flatMap(sl.info(_)(msg))
    def warn(ctx: Map[String, String])(msg: => String): F[Unit] =
      modify(ctx).flatMap(sl.warn(_)(msg))
    def error(ctx: Map[String, String])(msg: => String): F[Unit] =
      modify(ctx).flatMap(sl.error(_)(msg))

    def isTraceEnabled: F[Boolean] = sl.isTraceEnabled
    def isDebugEnabled: F[Boolean] = sl.isDebugEnabled
    def isInfoEnabled: F[Boolean]  = sl.isInfoEnabled
    def isWarnEnabled: F[Boolean]  = sl.isWarnEnabled
    def isErrorEnabled: F[Boolean] = sl.isErrorEnabled

    def error(t: Throwable)(message: => String): F[Unit] =
      defaultCtx.flatMap(sl.error(_, t)(message))
    def warn(t: Throwable)(message: => String): F[Unit] =
      defaultCtx.flatMap(sl.warn(_, t)(message))
    def info(t: Throwable)(message: => String): F[Unit] =
      defaultCtx.flatMap(sl.info(_, t)(message))
    def debug(t: Throwable)(message: => String): F[Unit] =
      defaultCtx.flatMap(sl.debug(_, t)(message))
    def trace(t: Throwable)(message: => String): F[Unit] =
      defaultCtx.flatMap(sl.trace(_, t)(message))

    def error(ctx: Map[String, String], t: Throwable)(message: => String): F[Unit] =
      modify(ctx).flatMap(sl.error(_)(message))
    def warn(ctx: Map[String, String], t: Throwable)(message: => String): F[Unit] =
      modify(ctx).flatMap(sl.warn(_)(message))
    def info(ctx: Map[String, String], t: Throwable)(message: => String): F[Unit] =
      modify(ctx).flatMap(sl.info(_)(message))
    def debug(ctx: Map[String, String], t: Throwable)(message: => String): F[Unit] =
      modify(ctx).flatMap(sl.debug(_)(message))
    def trace(ctx: Map[String, String], t: Throwable)(message: => String): F[Unit] =
      modify(ctx).flatMap(sl.trace(_)(message))
  }
}
