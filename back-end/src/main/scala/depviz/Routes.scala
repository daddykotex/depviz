package depviz

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import depviz.{html => dhtml}
import depviz.BuildInfo
import org.http4s.StaticFile
import fs2.io.file.{Path => FS2Path, Files}

object Routes {
  import dhtml.instances._

  def htmlRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root =>
      for {
        resp <- Ok(dhtml.Home.content)
      } yield resp
    }
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
}
