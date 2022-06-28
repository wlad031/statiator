package dev.vgerasimov.statiator

import com.hubspot.jinjava.lib.fn.{ELFunctionDefinition, Functions}

//class MyFuncClass {
//  def apply(s: String, s1: String) = "KEK "
//}
import dev.vgerasimov.example.MyFuncClass
object Main {

  trait Base(val msg: String)
  class A extends Base("Hello")
  class B extends Base("Dotty!")

  def main(args: Array[String]): Unit = {

//  import org.fusesource.scalate.TemplateEngine
//
//  val sourceDataPath = new java.io.File("/Users/vgerasimov/Projects/statiator/src/main/scala/simple_example.mustache").getCanonicalPath
//  val engine = new TemplateEngine
//  val someAttributes = Map(
//   "programming_language" -> "Scala",
//   "code_description" -> "pretty"
//  )
//  println(engine.layout(sourceDataPath, someAttributes))

    import com.google.common.collect.Maps
    import com.hubspot.jinjava.Jinjava
    val jinjava = MyFuncClass.jinjava1
   val k = Functions.RANGE_LIMIT
//    jinjava
//      .getGlobalContext()
//      .registerFunction(
//        new ELFunctionDefinition(
//          "foo",
//          "bar",
//          MyFuncClass.getClass,
//          "apply",
//          classOf[Int]
//        )
//      )
    import com.hubspot.jinjava.lib.fn.{ ELFunctionDefinition, Functions }
    jinjava
      .getGlobalContext()
      .registerFunction(
        new ELFunctionDefinition(
          "",
          "gen",
          classOf[MyFuncClass],
          "apply",
          classOf[String],
        )
      )
//  jinjava.setResourceLocator
    val context = Maps.newHashMap[String, String]
    context.put("name_1", "Jared")
    val renderedTemplate = jinjava.render("""
      |{{ gen('42') }}
      |<div>Hello, {{ name_1 }}!</div>""".stripMargin, context)
    println(renderedTemplate)
  }
}
