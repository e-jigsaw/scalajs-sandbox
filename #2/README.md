前回に引き続いて今回は Scala.js の DOM ライブラリを見ていきます。

# DOM ライブラリを使っての HelloWorld

まずは、DOM のライブラリを読み込めるように `build.sbt` に `libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"` を追記します。

`build.sbt`:

```
scalaJSSettings

name := "Scala.js Tutorial"

scalaVersion := "2.11.2"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"
```

`%%%` ってなんだろうなとおもっていると、注釈がありました。

> Note the %%% (instead of the usual %%) which will add the current Scala.js version to the artifact name.

より厳密な `%%` ということっぽいですね。JavaScript の `==` と `===` に似てますね。(似てない？)

これで DOM のライブラリが使えるようになったと信じて Scala のコードを書き換えていきます。

`src/main/scala/tutorial/webapp/TutorialApp.scala`:

```scala
package tutorial.webapp

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document

object TutorialApp extends JSApp {
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
  def main(): Unit = {
    appendPar(document.body, "Hello World!!")
  }
}
```

チュートリルを写経しました。コードから察するに `<p>` をつくってそこにテキストを放り込むコードのようですね。さて前回めんどくさくて html を用意してなかったので確認する環境がないのでさくっと html を準備します。生の html を書くことが宗教上の理由で禁じられているので jade で書くのをお許しください。

`src/web/index.jade`:

```jade
doctype html
html
  head
    title scalajs test
    script(src='scala-js-tutorial-fastopt.js', type='text/javascript')
  body
    h1 scalajs test
    script(type='text/javascript') tutorial.webapp.TutorialApp().main();
```

めんどくさかったのでスクリプトタグに起動コマンドを直書きしてしまいました。ついでに gulp で自動的にサーブするようにしました。

`src/web/gulpfile.coffee`:

```coffee
gulp = require 'gulp'
jade = require 'gulp-jade'
conn = require 'gulp-connect'

paths =
  copy: ['../../target/scala-2.11/scala-js-tutorial-fastopt.js']
  jade: ['index.jade']
  dest: 'build'

gulp.task 'copy', ->
  gulp.src paths.copy
    .pipe gulp.dest(paths.dest)

gulp.task 'jade', ->
  gulp.src paths.jade
    .pipe jade()
    .pipe gulp.dest(paths.dest)

gulp.task 'default', ['copy', 'jade']
gulp.task 'watch', ['default'], ->
  gulp.watch paths.copy, ['copy']
  gulp.watch paths.jade, ['jade']
  conn.server
    root: paths.dest
```

あとはよしなにモジュール類をインストールして `$ gulp watch` して `$ open http://localhost:8080` すれば

```html
<!DOCTYPE html>
<html>
  <head>
    <title>scalajs test</title>
    <script src="scala-js-tutorial-fastopt.js" type="text/javascript"></script>
  </head>
  <body>
    <h1>scalajs test</h1>
    <script type="text/javascript">tutorial.webapp.TutorialApp().main();</script>
    <p>Hello World!!</p>
  </body>
</html>
```

やりました！！ `<p>` タグが追加されていることが分かります。

# scalatags を使って scala のコード中に html を書く

Scala.js のチュートリアルでは「`<p>` タグ入ったやろ？すごいやろ？」ぐらいで終わってるので、もうちょっと DOM まわりを調べました。Scala.js には [Scala-JS-Fiddle](http://www.scala-js-fiddle.com/) という Scala.js をブラウザ上でさくっと試せるサービスがあり、サンプルなどを確認することができます。これのランディングページのコードを確認すると

```scala
  def render() = {
    Page.clear()

    println(
      div(
        padding:="10px",
        id:="page",
        h1(
          marginTop:="0px",
          img(src:="/Shield.svg", height:="40px", marginBottom:="-10px"),
          " Scala.jsFiddle"
        ),
        p(
          "Scala.jsFiddle is an online scratchpad to try out your ",
          a(href:="http://www.scala-js.org/", "Scala.js "),
          "snippets. Enter Scala code on the left and see the results on the right, ",
          "after the Scala is compiled to Javascript using the Scala.js compiler."
        ),
        ul(
          li(green("Ctrl/Cmd-Enter"), ": compile and execute"),
          li(green("Ctrl/Cmd-Shift-Enter"), ": compile and execute optimized code"),
          li(green("Ctrl/Cmd-Space"), ": autocomplete at caret"),
          li(green("Ctrl/Cmd-J"), ": show the generated Javascript code"),
          li(green("Ctrl/Cmd-Shift-J"), ": show the generated code, optimized by Google Closure"),
          li(green("Ctrl/Cmd-S"), ": save the code to a public Gist"),
          li(green("Ctrl/Cmd-E"), ": export your code to a stand-alone web page")
        ),
...
```

というような調子で Scala のコード中に HTML のコードのようなものが混ざっているのが見受けられます。ドキュメントを読むと scalatags というライブラリを使ってこれを実現しているようです。

https://github.com/lihaoyi/scalatags

ちなみに、JavaScript でも [medikoo/domjs](https://github.com/medikoo/domjs) という似たライブラリがあります。CoffeeScript には [mauricemach/coffeekup](https://github.com/mauricemach/coffeekup) があります。詳しくは両ライブラリの README をお読みください。

さて、ローカルでも scalatags を使って DOM を生成してみたいとおもいます。scalatags の README に従って `build.sbt` に `libraryDependencies += "com.scalatags" %%% "scalatags" % "0.4.2"` を追加します。ちなみに、何も考えずに

```
libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"
libraryDependencies += "com.scalatags" %%% "scalatags" % "0.4.2"
```

として `reload` すると

```
[error] Error parsing expression.  Ensure that settings are separated by blank lines.
```

とエラーが出るので

```
libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"

libraryDependencies += "com.scalatags" %%% "scalatags" % "0.4.2"
```

と空行をいれてやらないといけないようです。これで `reload` が通るので実際にコードを書いていきます。

`src/main/scala/tutorial/webapp/TutorialApp.scala`:

```scala
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
```

すごい微妙なのは承知ですが、DOM と html を渡すと DOM の `innerHTML` に html を設定する `updateInnerHTML` という関数を定義しました。`innerHTML` は DOM じゃなくて Element のプロパティなので `targetNode` の型を変えてやる必要がありました。

次に、`header` という関数で scalatags を使って html を出力します。JS 脳で何も考えずに

```scala
// Not works
def header(): = {
  div(
    h1(
      "It works!"
    )
  )
}
```

などと書いていたら型がないよと言われ、`String` を付けたら `div` の返り値が `HTMLDivElement` やでとエラーが出たので `toString` を付けました。あと、JS 脳なので関数を実行するときには `()` を付けていたんですが Scala では省略できることに気付いて勉強になりました。Ruby っぽいですね。

さて、先ほど gulp で fastOpt の JavaScript ファイルをウォッチするようにしておいたので自動的にウェブの方のビルドディレクトリに生成された JavaScript のコードがコピーされています。ちらっとファイルを見てみると、行数にして2万2千行、サイズでいうと1MBの JavaScript のファイルが生成されていました。強い。

さてこれをブラウザで実行してみると...!

```html
<!DOCTYPE html>
<html>
  <head>
    <title>scalajs test</title>
    <script src="scala-js-tutorial-fastopt.js" type="text/javascript"></script>
  </head>

  <body>
    <div>
      <h1>It works!</h1>
    </div>
  </body>
</html>
```

ヤッター！

# Scala.js でインタラクティブな画面をつくる

ここまでやってきた集大成としてなにか簡単なインタラクションのある画面をつくってみたいとおもいます。

## 完成図

(ここにGIFをいれる)

新年のめでたい気持ちを表すページを作ってみたいとおもいます。めでたいので紅白幕がチカチカします。

## ソースコード

`src/main/scala/tutorial/webapp/TutorialApp.scala`:

```scala
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
```

Scala 初心者が調べながら一生懸命書いたコードです、今年もよろしくお願いします。

## コードの説明

HTML からは以前までのサンプル同様に `tutorial.webapp.TutorialApp().main()` をキックするだけで特になにも書いていません。一方、`main()` では、`layout` を `body` に挿入しているだけです。さて、`layout` でなにをやっているかというと

```scala
def layout(): dom.Node = {
  div(
    style := "position: relative;",
    redWhiteNode,
    greetingNode,
    redWhiteNode
  ).render
}
```

名前が示す通り、全体の大枠を作って返すということをやっています。`appendChild` が `dom.Node` を引数にとるので最後に `render` して大枠の `dom.Node` を返しています。

続いて、先に `greetingNode` を見ていきます。

```scala
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
```

ここでは、名前を入力したら「あけましておめでとうございます！！○○」というヘッダーを返す処理を担っています。それぞれ `greeting, name, nameLabel` で当該の要素を定義して、最後の `div` 関数で3つを組み合わせた要素を生成して返します。ここで

```scala
name.onkeyup = (event: dom.Event) => {
  greeting.textContent = "Hello! " + name.value
}
```

でキー入力が完了したイベントを受け取ってヘッダーテキストを更新する処理をしています。`=>` は無名関数のようなものなんでしょうか。ここで注意しなければならないのが `onkeyup` や `textContent`, `value` で、これらはそれぞれ `dom.HTMLElement` や `dom.HTMLInputElement` の関数なので先に `render` しないとコンパイルが通らないところです。

最後に `redWhiteNode` を見ていきます。

```scala
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
```

`redWhiteNode` では、まず適当な数の要素を用意し、それを一定間隔で背景色を変えることで紅白幕っぽいものを実現しています。Scala では for 文がちゃんと値を返してくれるので

```scala
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
```

という感じで要素群が生成できます。これで生成した要素に対して

```scala
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
}, 200)`
```

のブロックで200ミリ秒毎にそれぞれの紅白を反転させています。`dom` は JavaScript でいう `window` を `extend` しているので `dom.setInterval` と書くことで JavaScript の `setInterval` を呼び出すことができます。ここでも `getAttribute` や `setAttribute` するために予め `render` しています。

# todo
