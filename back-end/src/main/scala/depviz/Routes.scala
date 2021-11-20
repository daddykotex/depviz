package depviz

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import depviz.{html => dhtml}
import org.http4s.StaticFile

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

  def assetsRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case req @ GET -> "assets" /: path =>
      println(path)
      StaticFile
        .fromResource("/assets/" + path.toString, Some(req))
        .getOrElseF(NotFound(dhtml.NotFound.content))
    }
  }
}
