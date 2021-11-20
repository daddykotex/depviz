package depviz.html

import org.http4s.EntityEncoder
import org.http4s.headers.`Content-Type`
import org.http4s.MediaType
import scalatags.Text

object instances {
  implicit def htmlEntityEncoder[F[_]]
      : EntityEncoder[F, Text.TypedTag[String]] =
    EntityEncoder
      .stringEncoder[F]
      .contramap[Text.TypedTag[String]](_.toString)
      .withContentType(`Content-Type`(MediaType.text.html))
}
