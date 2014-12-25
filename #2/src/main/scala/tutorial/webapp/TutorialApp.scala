package tutorial.webapp

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import scalatags.JsDom.all._

object TutorialApp extends JSApp {
  def updateInnerHTML(targetNode: dom.Element, html: String): Unit = {
    targetNode.innerHTML = html
  }

  def main(): Unit = {
    updateInnerHTML(document.body, header)
  }

  def header(): String = {
    div(
      h1(
        "It works!"
      )
    ).toString
  }
}
