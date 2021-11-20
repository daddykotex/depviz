package depviz.html

import scalatags.Text.all._
import scalatags.Text

object Home {
  val content: Text.TypedTag[String] = html(
    head(
      script(
        src := "/assets/js/fast-opt/main.js"
      ) // use buildinfo to compute this path
    ),
    body(
      h1("This is my title"),
      div(
        p("This is my first paragraph"),
        p("This is my second paragraph")
      )
    )
  )
}
