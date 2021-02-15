package com.github.hongee.importthis.backends

import sttp.client3._
import sttp.client3.httpclient.fs2._
import cats.effect.{Blocker, ConcurrentEffect, ContextShift}
import cats.syntax.all._
import com.github.hongee.importthis.{ImportData, Params, RepositoryOptions}
import org.jsoup.Jsoup

import scala.jdk.CollectionConverters._

case class ScaladexBackend[F[_]: ConcurrentEffect: ContextShift](blocker: Blocker) extends Backend[F] {
  private val client = HttpClientFs2Backend[F](blocker)

  private val asHtml = asStringAlways.map(ResponseAs.deserializeCatchingExceptions(Jsoup.parse))

  private val asData = asHtml.map { html =>
    html.map {
      _.select(".list-result > .item-list").iterator().asScala.map { el =>
        val title = Option(el.selectFirst("h4")).fold("")(_.text())
        RepositoryOptions(title)
      }.toSeq
    }
  }

  private val asSbtInsertionString = asHtml.map { html =>
    html.map { res =>
      Option(res.selectFirst("#copy-sbt")).fold("")(_.text())
    }
  }

  private def buildQueryRequest(params: Params) = basicRequest
    .get(uri"https://index.scala-lang.org/search?q=${params.searchQuery}")
    .response(asData)

  private def buildLookupRequest(data: RepositoryOptions) = basicRequest
    .get(uri"https://index.scala-lang.org/${data.org}/${data.path}")
    .followRedirects(true)
    .response(asSbtInsertionString)

  override def query(params: Params): F[Seq[RepositoryOptions]] = for {
    c <- client
    response <- c.send(buildQueryRequest(params))
    lifted <- response.body.liftTo[F]
  } yield lifted


  override def fetch(data: RepositoryOptions): F[ImportData] = for {
    c <- client
    response <- c.send(buildLookupRequest(data))
    lifted <- response.body.liftTo[F]
    id = ImportData(response.history.lastOption.fold("")(_.request.uri.toString()), lifted)
  } yield id
}
