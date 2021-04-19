package com.pagero.cats

import scala.concurrent.Future

object FuTest extends App {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val f1 = for {
    r <- future1()
    _ <- future2()
  } yield None

//  future3()

  def future1() = {
    Future{
      Thread.sleep(1000)
      println("Future 1")
    }
  }

  def future2() = {
    Future{
      Thread.sleep(10)
      println("Future 2")
    }
  }

  def future3(): Unit = {
    Future{
      Thread.sleep(1000)
      println("Future 3")
    }
  }

  while (true){
    Thread.sleep(1)
  }

}
