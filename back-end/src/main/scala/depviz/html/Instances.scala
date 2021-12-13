package depviz.html

import org.http4s.Charset
import org.http4s.EntityEncoder
import org.http4s.MediaType
import org.http4s.headers.`Content-Type`
import scalatags.Text

object instances {
  implicit def htmlEntityEncoder[F[_]]: EntityEncoder[F, Text.TypedTag[String]] =
    EntityEncoder
      .stringEncoder[F]
      .contramap[Text.TypedTag[String]](_.toString)
      .withContentType(`Content-Type`(MediaType.text.html).withCharset(Charset.`UTF-8`))
}
