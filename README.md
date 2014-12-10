こんにちは！編集部の新卒の小林です。普段は JavaScript をよく書くので Scala はあまり詳しくないのですが編集部で手伝いをしています。以後よろしくお願いします！

さてみなさん Javascript は好きですか？え？嫌い？そうですか、そうですよね...。そんなみなさんにオススメなのが [Scala.js](http://www.scala-js.org/) です。Scala を書いたら JavaScript が出てくるので、サーバサイドで Play を使ってるんだけどフロントは JavaScript なのでロジックが二重化していて...みたいなときとかに嬉しい気がします。

# Scala のセットアップ

それでは、さっそく[チュートリアル](http://www.scala-js.org/doc/tutorial.html)をやってみて雰囲気を掴んでいこうとおもいます。Scala 初心者なのでそもそもマシンに Scala の環境が整っていないので、まずはそのセットアップからはじめます。

Mac + Homebrew を使っているので `brew` で Scala まわりのパッケージをインストールします。

```
✈ brew install scala
✈ brew install sbt
```

おもむろに `sbt -v` するといろいろダウンロードがはじまるので待ちます。

```
✈ sbt -v
[process_args] java_version = '1.8.0'
# Executing command line:
java
-Xms1024m
-Xmx1024m
-XX:ReservedCodeCacheSize=128m
-jar
/usr/local/Cellar/sbt/0.13.6/libexec/sbt-launch.jar

Getting org.scala-sbt sbt 0.13.6 ...
```

とおもったら `sbt` の REPL が立ち上がるので `Ctrl+C` で抜けます。

# HelloWorld やっていくぞ

チュートリアルに従って、HelloWorld のアプリケーションを作っていきます。指示どおりにファイルを作っていきます。

`src/main/scala/tutorial/webapp/TutorialApp.scala`:

```
package tutorial.webapp

import scala.scalajs.js.JSApp

object TutorialApp extends JSApp {
  def main(): Unit = {
    println("Hello world!!")
  }
}
```

普段 Atom をエディタに使っているので、`.scala` のファイルを作ったらシンタックスハイライトが効かなかったので Scala の  [Language Pack](https://github.com/jroesch/language-scala) をインストールしました。IntelliJ とかいう高級な IDE は持ってません。Atom でがんばります。

`sbt` でビルドします。

そうするとなにやらエラーが出ます。

```
✈ sbt
[info] Set current project to scalajs (in build file:/Users/***/.ghq/src/local/scalajs/)
> run
[info] Updating {file:/Users/***/.ghq/src/local/scalajs/}scalajs...
[info] Resolving org.fusesource.jansi#jansi;1.4 ...
[info] Done updating.
[info] Compiling 1 Scala source to /Users/***/.ghq/src/local/scalajs/target/scala-2.10/classes...
[error] /Users/***/.ghq/src/local/scalajs/src/main/scala/tutorial/webapp/TutorialApp.scala:3: object scalajs is not a member of package scala
[error] import scala.scalajs.js.JSApp
[error]              ^
[error] /Users/***/.ghq/src/local/scalajs/src/main/scala/tutorial/webapp/TutorialApp.scala:5: not found: type JSapp
[error] object TutorialApp extends JSapp {
[error]                            ^
[error] two errors found
[error] (compile:compile) Compilation failed
[error] Total time: 2 s, completed 2014/12/10 9:33:51
```

ほうほうなるほど、`scalajs` がないっぽいですね。そういえば、`sbt` のセットアップのところをすっ飛ばしていました。セットアップします。

`project/plugins.sbt`:

```
addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.6")
```

`build.sbt`:

```
scalaJSSettings

name := "Scala.js Tutorial"

scalaVersion := "2.11.2"
```

`project/build.properties`:

```
sbt.version=0.13.5
```

ふう、設定ファイルが多いですね。それでは、もう一度 `sbt` の REPL を起動しなおしてビルドしていきます。

```
✈ sbt
Getting org.scala-sbt sbt 0.13.5 ...
downloading https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt/0.13.5/jars/sbt.jar ...
[SUCCESSFUL ] org.scala-sbt#sbt;0.13.5!sbt.jar (1114ms)
...
```

どうやら、`project/build.properties` で指定した `sbt` のバージョンと違うようで `sbt` のダウンロードがはじまりました。`sbt` のダウンロードが終わると `scalajs` のダウンロードがはじまりました。

```
[info] Loading project definition from /Users/***/.ghq/src/local/scalajs/project
[info] Updating {file:/Users/***/.ghq/src/local/scalajs/project/}scalajs-build...
[info] Resolving org.fusesource.jansi#jansi;1.4 ...
[info] downloading http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/org.scala-lang.modules.scalajs/scalajs-sbt-plugin/scala_2.10/sbt_0.13/0.5.6/jars/scalajs-sbt-plugin.jar ...
[info]  [SUCCESSFUL ] org.scala-lang.modules.scalajs#scalajs-sbt-plugin;0.5.6!scalajs-sbt-plugin.jar (4815ms)
...
```

`scalajs` のダウンロードが終わると準備完了っぽい感じになりました。

```
...
[info] Done updating.
[info] Set current project to Scala.js Tutorial (in build file:/Users/***/.ghq/src/local/scalajs/)
```

今度こそ `run` します。

```
> run
[info] Updating {file:/Users/***/.ghq/src/local/scalajs/}scalajs...
[info] Resolving org.sonatype.oss#oss-parent;7 ...
[info] downloading http://repo1.maven.org/maven2/org/scala-lang/scala-library/2.11.2/scala-library-2.11.2.jar ...
[info]  [SUCCESSFUL ] org.scala-lang#scala-library;2.11.2!scala-library.jar (7176ms)
...
```

そうすると、まだ足りないようでダウンロードがはじまりました。それが終わると

```
...
[info] Compiling 1 Scala source to /Users/***/.ghq/src/local/scalajs/target/scala-2.11/classes...
[info] Running tutorial.webapp.TutorialApp
Hello world!!
[success] Total time: 4 s, completed 2014/12/10 9:47:40
```

おおお、やったぜ！

さらに、`fastOptJS` というタスクを実行すると JavaScript のコードが吐き出されるようなので実行してみます。

```
> fastOptJS
[info] Fast optimizing /Users/***/.ghq/src/local/scalajs/target/scala-2.11/scala-js-tutorial-fastopt.js
[success] Total time: 2 s, completed 2014/12/10 9:48:42
```

ふむふむ、`target/scala-2.11/scala-js-tutorial-fastopt.js` というファイルが生成されたようです。おもむろに実行してみると

```
✈ node target/scala-2.11/scala-js-tutorial-fastopt.js
```

なにも出ずに終了するのですが、どうやら `main` メソッドを呼んでやらないといけないようです。チュートリアルでは HTML を書いてブラウザで実行するように書かれていますが、面倒くさいのでファイルに追記してささっと確認します。

```
...
  s_Equals: 1,
  jl_RuntimeException: 1,
  jl_Exception: 1,
  jl_Throwable: 1,
  Ljava_io_Serializable: 1,
  O: 1
});
ScalaJS.c.sjs_js_JavaScriptException.prototype.$classData = ScalaJS.d.sjs_js_JavaScriptException;

tutorial.webapp.TutorialApp().main();

//# sourceMappingURL=scala-js-tutorial-fastopt.js.map
```

今度こそ

```
✈ node target/scala-2.11/scala-js-tutorial-fastopt.js
Hello world!!
```

やりました！3000行の HelloWorld の JavaScript のコードが錬成されました。ランタイムのコードを追おうとおもったんですが、長すぎて普通に心が折れそうになったのでとりあえず `main` っぽいところを読んでみると

```
ScalaJS.c.Ltutorial_webapp_TutorialApp$.prototype.$$js$exported$meth$main__O = (function() {
  var this$2 = ScalaJS.m.s_Console();
  var this$3 = this$2.outVar$2;
  ScalaJS.as.Ljava_io_PrintStream(this$3.tl$1.get__O()).println__O__V("Hello world!!")
});
```

`println__O__V` を読んでみると

```
ScalaJS.c.Ljava_io_PrintStream.prototype.println__O__V = (function(x) {
  this.print__O__V(x);
  this.write__I__V(10)
});
```

`print__O__V` があやしいですね。

```
ScalaJS.c.Ljava_io_PrintStream.prototype.print__O__V = (function(o) {
  if ((o === null)) {
    ScalaJS.i.jl_JSConsoleBasedPrintStream$class__print__jl_JSConsoleBasedPrintStream__T__V(this, "null")
  } else {
    var s = ScalaJS.objectToString(o);
    ScalaJS.i.jl_JSConsoleBasedPrintStream$class__print__jl_JSConsoleBasedPrintStream__T__V(this, s)
  }
});
```

どうやら `jl_JSConsoleBasedPrintStream$class__print__jl_JSConsoleBasedPrintStream__T__V` というどえらい長い関数がそれっぽいです。

```
ScalaJS.i.jl_JSConsoleBasedPrintStream$class__print__jl_JSConsoleBasedPrintStream__T__V = (function($$this, s) {
  var rest = ((s === null) ? "null" : s);
  while ((!ScalaJS.i.sjsr_RuntimeString$class__isEmpty__sjsr_RuntimeString__Z(rest))) {
    var nlPos = ScalaJS.i.sjsr_RuntimeString$class__indexOf__sjsr_RuntimeString__T__I(rest, "\n");
    if ((nlPos < 0)) {
      $$this.java$lang$JSConsoleBasedPrintStream$$buffer$und$eq__T__V((("" + $$this.java$lang$JSConsoleBasedPrintStream$$buffer__T()) + rest));
      $$this.java$lang$JSConsoleBasedPrintStream$$flushed$und$eq__Z__V(false);
      rest = ""
    } else {
      $$this.doWriteLine__T__V((("" + $$this.java$lang$JSConsoleBasedPrintStream$$buffer__T()) + ScalaJS.i.sjsr_RuntimeString$class__substring__sjsr_RuntimeString__I__I__T(rest, 0, nlPos)));
      $$this.java$lang$JSConsoleBasedPrintStream$$buffer$und$eq__T__V("");
      $$this.java$lang$JSConsoleBasedPrintStream$$flushed$und$eq__Z__V(true);
      rest = ScalaJS.i.sjsr_RuntimeString$class__substring__sjsr_RuntimeString__I__T(rest, ((nlPos + 1) | 0))
    }
  }
});
```

`doWriteLine__T__V` が書き出しをしている気がします。

```
ScalaJS.c.jl_StandardErrPrintStream$.prototype.doWriteLine__T__V = (function(line) {
  if ((!ScalaJS.uZ((!ScalaJS.g["console"])))) {
    if ((!ScalaJS.uZ((!ScalaJS.g["console"]["error"])))) {
      ScalaJS.g["console"]["error"](line)
    } else {
      ScalaJS.g["console"]["log"](line)
    }
  }
});
```

`ScalaJS.g` までたどりつきました！！

```
var ScalaJS = {
  // Fields
  g: (typeof global === "object" && global && global["Object"] === Object) ? global : this, // Global scope
...
```

これらをつかって `console.log('Hellow world!!')` のようなものを再現しているようですね。悪魔的だ...。

# まとめ

Scala.js を使って JavaScript で HelloWorld するところまでチュートリアルに沿って確認しました。次回は DOM のライブラリを使って DOM を操作する JavaScript のコードを見ていきたいとおもいます。
