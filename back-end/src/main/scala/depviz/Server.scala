package depviz

import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Server {

  def stream[F[_]: Async]: Stream[F, Any] = {
    // asIntanceOf to stop the dead code warning.
    def hacked = Resource.eval(Async[F].never).asInstanceOf[Resource[F, Any]]

    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      _ = locally(client)
      helloWorldAlg = HelloWorld.impl[F]

      httpApp = (
        Routes.htmlRoutes[F] <+>
          Routes.assetsRoutes[F]
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, false)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >> hacked
      )
    } yield exitCode
  }.drain
}
