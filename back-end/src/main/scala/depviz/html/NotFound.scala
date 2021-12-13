package depviz.html

import scalatags.Text
import scalatags.Text.all._

object NotFound {
  val content = html(
    head(
      Text.tags2.title("Page not found")
    ),
    body(
      h1("Page not found.")
    )
  )
}
