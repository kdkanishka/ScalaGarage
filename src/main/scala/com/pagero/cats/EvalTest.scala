package com.pagero.cats

import cats.Eval
// import cats.Eval

import cats.implicits._

object EvalTest extends App {

  def evalTest() = {
    val eager = Eval.now {
      println("Running the eager calc")
      1 + 2
    }

    val lazyVal = Eval.later {
      println("Running the lazy val calc")
      1 + 3
    }

    println("Hello!")
    println(eager.value)
    println(lazyVal.value)
    println(lazyVal.value)
  }

  def even(n: Int): Eval[Boolean] =
    Eval.always(n == 0).flatMap {
      case true => Eval.now(true)
      case false => odd(n - 1)
    }

  def odd(n: Int): Eval[Boolean] =
    Eval.always(n == 0).flatMap {
      case true => Eval.now(false)
      case false => even(n - 1)
    }

  evalTest()

  println(odd(199999).value)
}
