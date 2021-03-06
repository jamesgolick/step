package com.thinkminimo.step

import java.util.NoSuchElementException
import org.scalatest.matchers.ShouldMatchers

object ParamsTestServlet {
  val NoSuchElement = "No Such Element"
}

class ParamsTestServlet extends Step {
  import ParamsTestServlet._

  get("/multiParams/:key") {
    multiParams(params("key")).mkString("[",",","]")
  }

  get("/params/:key") {
    try {
      params(params("key"))
    }
    catch {
      case _: NoSuchElementException => NoSuchElement
      case e => throw e
    }
  }
}

class ParamsTest extends StepSuite with ShouldMatchers {
  route(classOf[ParamsTestServlet], "/*")

  test("supports multiple parameters") {
    get("/multiParams/numbers", "numbers" -> "one", "numbers" -> "two", "numbers" -> "three") {
      body should equal ("[one,two,three]")
    }
  }

  test("unknown multiParam returns an empty seq") {
    get("/multiParams/oops") {
      status should equal (200)
      body should equal ("[]")
    }
  }

  test("params returns first value when multiple values present") {
    get("/params/numbers", "numbers" -> "one", "numbers" -> "two", "numbers" -> "three") {
      body should equal ("one")
    }
  }

  test("params on unknown key throws NoSuchElementException") {
    get ("/params/oops") {
      body should equal (ParamsTestServlet.NoSuchElement)
    }
  }
}