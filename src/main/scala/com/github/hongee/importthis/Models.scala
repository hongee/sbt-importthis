package com.github.hongee.importthis

import cats.Show

final case class ImportData(link: String, importLine: String)

object ImportData {
  implicit val show: Show[ImportData] = Show.show[ImportData](id =>
    s"Link: ${id.link}\n${id.importLine}"
  )
}

final case class RepositoryOptions(repositoryName: String) {
  lazy val (org, path) = repositoryName.split("/").toList match {
    case h :: t :: Nil => (h, t)
    case _ => (repositoryName, "")
  }
}

object RepositoryOptions {
  implicit val show: Show[RepositoryOptions] = Show.show[RepositoryOptions](_.repositoryName)
}

final case class Params(
  searchQuery: String
)

object Params {
  val empty: Params = Params("")
}
