package com.github.hongee.importthis

import cats.data.NonEmptyList
import cats.effect.Sync
import cats.syntax.all._
import scopt.{OParser, OParserBuilder}
import smenu.Menu.singleChoiceMenu

object CliApp {
  def parseArgs[F[_]: Sync](args: Seq[String]): F[Params] = {
    val (config, effects) = OParser.runParser(paramParser, args, Params.empty)
    for {
      _ <- Sync[F].delay(OParser.runEffects(effects))
      matchedConfig <- config match {
        case None         => Sync[F].raiseError(new IllegalArgumentException("failed to parse parameters"))
        case Some(config) => Sync[F].pure(config)
      }
    } yield matchedConfig
  }

  def selectOpt[F[_]: Sync](choices: Seq[RepositoryOptions]): F[RepositoryOptions] = {
    NonEmptyList.fromFoldable(choices) match {
      case Some(value) =>     singleChoiceMenu(s"found ${choices.size} options:", value)
      case None => Sync[F].raiseError(new IllegalArgumentException("no results found"))
    }
  }

  private val paramBuilder: OParserBuilder[Params] = OParser.builder[Params]
  private val paramParser: OParser[Unit, Params] = {
    import paramBuilder._
    OParser.sequence(
      programName("import-this"),
      head("import-this", "1.0"),
      arg[String]("query")
        .required()
        .validate(q => if (q.isEmpty) failure("the query cannot be empty") else success)
        .action((arg, c) => c.copy(searchQuery = arg))
        .text("the package to search for")
    )
  }

}
