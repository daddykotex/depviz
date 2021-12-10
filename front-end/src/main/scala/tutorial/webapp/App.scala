package tutorial.webapp

import org.scalajs.dom
import scala.scalajs.js
import org.scalajs.dom.document

object TutorialApp {
  private val dummyTree: js.Dynamic = js.Dynamic.global.selectDynamic("tree_data")

  def filter(value: String, divTree: dom.html.Div, tree: js.Dynamic): Unit = {
    println(value)
    println(divTree)
    println(tree)
  }

  def setupUI(): Unit = {
    val input = document.createElement("input").asInstanceOf[dom.html.Input]
    val divTree = document.createElement("div").asInstanceOf[dom.html.Div]
    input.addEventListener(
      "keyup",
      { (_: dom.KeyboardEvent) => filter(input.value, divTree, dummyTree) }
    )

    document.body.appendChild(input)
    document.body.appendChild(divTree)

    ()
  }

  def main(args: Array[String]): Unit = {
    document.addEventListener(
      "DOMContentLoaded",
      { (_: dom.Event) => setupUI() }
    )
  }
}
