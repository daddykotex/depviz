package depviz

import cats.Applicative
import cats.implicits._

trait HelloWorld[F[_]] {
  def hello(n: HelloWorld.Name): F[HelloWorld.Greeting]
}

object HelloWorld {
  implicit def apply[F[_]](implicit ev: HelloWorld[F]): HelloWorld[F] = ev

  final case class Name(name: String) extends AnyVal
  final case class Greeting(greeting: String) extends AnyVal

  def impl[F[_]: Applicative]: HelloWorld[F] = new HelloWorld[F] {
    def hello(n: HelloWorld.Name): F[HelloWorld.Greeting] =
      Greeting("Hello, " + n.name).pure[F]
  }
}
