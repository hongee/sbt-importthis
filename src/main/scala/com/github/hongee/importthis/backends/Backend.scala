package com.github.hongee.importthis.backends

import com.github.hongee.importthis.{ImportData, Params, RepositoryOptions}

trait Backend[F[_]] {
  def query(params: Params): F[Seq[RepositoryOptions]]

  def fetch(data: RepositoryOptions): F[ImportData]
}
