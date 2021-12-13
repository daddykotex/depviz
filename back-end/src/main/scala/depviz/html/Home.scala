package depviz.html

import scalatags.Text
import scalatags.Text.all._

object Home {

  val content: Text.TypedTag[String] = html(
    head(
      script(src := "/assets/js/dummy-tree.js"),
      script(src := "https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"),
      script(src := "https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"),
      link(
        rel := "stylesheet",
        href := "https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"
      )
    ),
    body(
      script(src := "/assets/js/fast-opt/main.js") // use buildinfo to compute this path
    )
  )
}
