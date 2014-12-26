package tutorial.webapp

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import scalatags.JsDom.all._

object TutorialApp extends JSApp {
  def main(): Unit = {
    document.body.appendChild(layout)
  }

  def layout(): dom.Node = {
    div(
      style := "position: relative;",
      redWhiteNode,
      greetingNode,
      redWhiteNode
    ).render
  }

  def greetingNode(): dom.Node = {
    val rightMargin = "margin-right: 5px"

    val greeting = h1(
      "あけましておめでとうございます！！"
    ).render

    val name = input(
      "type".attr := "text",
      style := rightMargin,
      id := "name"
    ).render

    val nameLabel = label(
      "for".attr := "name",
      style := rightMargin,
      "Enter Your Name:"
    )

    name.onkeyup = (event: dom.Event) => {
      greeting.textContent = "Hello! " + name.value
    }

    div(
      greeting,
      p(
        nameLabel,
        name
      )
    ).render
  }

  def redWhiteNode(): dom.Node = {
    val barCount = 20
    val elmWidth = dom.innerWidth / barCount
    val height = "height: 150px;"
    val absolute = "position: absolute;"
    val relative = "position: relative;"
    val bars = for {
      i <- 0 until barCount
    } yield {
      val width = "width:" + elmWidth + "px;"
      val left  = "left:"  + (elmWidth * i) + "px;"
      val initStyle = absolute + width + height + left

      div(
        style := initStyle,
        "initStyle".attr := initStyle,
        "count".attr := i
      ).render
    }
    var flag = true

    dom.setInterval(() => {
      for {
        bar <- bars
      } yield {
        val biflag = if((bar.getAttribute("count").toInt % 2) == 1) true else false
        val bgFlag = if(flag) biflag else !biflag
        val bgColor = if(bgFlag) "background-color: red" else "background-color: white"
        bar.setAttribute("style", bar.getAttribute("initStyle") + bgColor)
      }
      flag = !flag
    }, 200)

    div(
      style := relative + height,
      bars
    ).render
  }
}
