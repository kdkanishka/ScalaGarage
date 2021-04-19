package com.pagero.auth

case class Request(data: String)

case class Response(data: String)

case class Action(f: Request => Response)

object PlayLike extends App {
  val action = Action {
    req => Response(s"Hello ${req.data}")
  }

  println(action.f(Request("oops")))
}
