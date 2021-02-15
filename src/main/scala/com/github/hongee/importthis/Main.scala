package com.github.hongee.importthis

import cats.Show
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import com.github.hongee.importthis.backends.ScaladexBackend

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

object Main extends IOApp {

  private val blocker = Blocker.liftExecutionContext(ExecutionContext.global)
  private val scaladexBackend = ScaladexBackend[IO](blocker)

  override def run(args: List[String]): IO[ExitCode] = {
    val app = for {
      args <- CliApp.parseArgs[IO](args)
      results <- scaladexBackend.query(args)
      selection <- CliApp.selectOpt[IO](results)
      copyable <- scaladexBackend.fetch(selection) <& IO(println("fetching your import statement..."))
      _ <- IO(println(Show[ImportData].show(copyable)))
    } yield ExitCode.Success

    app.handleErrorWith {
      case iae: IllegalArgumentException => IO(println(iae.getMessage)).as(ExitCode.Success)
      case NonFatal(e) => IO.raiseError(e)
    }
  }

}
