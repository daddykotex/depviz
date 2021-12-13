package depviz.routes

import cats.MonadThrow
import cats.effect._
import cats.implicits._
import io.circe.Decoder
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

object ApiRoutes {
  def coursier[F[_]: MonadThrow: Concurrent]: HttpRoutes[F] = {
    import Coursier._
    val dsl = new Http4sDsl[F] {}
    import dsl._
    implicit def completeEntityDecoder: EntityDecoder[F, Complete] = jsonOf[F, Complete]

    HttpRoutes.of[F] { case req @ POST -> Root / "complete" =>
      for {
        complete <- req.as[Complete]
        res <- Ok(complete.value)
      } yield res
    }
  }
}

private object Coursier {
  final case class Complete(value: String)
  implicit val completeDecoder: Decoder[Complete] = Decoder.forProduct1("value")(Complete(_))
}
