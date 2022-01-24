package depviz.routes

import cats.Monad
import cats.MonadThrow
import cats.effect._
import cats.implicits._
import depviz.BuildInfo
import depviz.{html => dhtml}
import fs2.io.file.Files
import fs2.io.file.{Path => FS2Path}
import org.http4s.HttpRoutes
import org.http4s.StaticFile
import org.http4s.dsl.Http4sDsl

object Routes {
  import dhtml.instances._

  def htmlRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root =>
      for {
        resp <- Ok(dhtml.Home.content)
      } yield resp
    }
  }

  def apiRoutes[F[_]: MonadThrow: Concurrent]: HttpRoutes[F] = {
    ApiRoutes.coursier[F]
  }

  def assetsRoutes[F[_]: Sync: Files]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case req @ GET -> "assets" /: path =>
      BuildInfo.assetsPath
        .map { f =>
          val fullPath = FS2Path.fromNioPath(f.toPath()) / "assets" / path.toString()
          println(fullPath.absolute.toString)
          StaticFile.fromPath(fullPath, Some(req))
        }
        .getOrElse(StaticFile.fromResource("/assets/" + path.toString, Some(req)))
        .getOrElseF(NotFound(dhtml.NotFound.content))
    }
  }

  def under[F[_]: Monad](routes: HttpRoutes[F])(path: String): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case req @ _ -> Root / path =>
      routes.run(req)
    }
  }
}
