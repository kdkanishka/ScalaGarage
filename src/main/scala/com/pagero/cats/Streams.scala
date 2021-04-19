package com.pagero.cats

import scala.annotation.tailrec
import scala.collection.immutable._
import scala.util.{Success, Try}


object Test extends App {
  val list = List(10, 20, 30, 40, 50)
  println(list)

  val ls2 = calc(list)
  println(ls2)

  if (list.length != ls2.length) {
    println("Unable to process it successfully")
  }


  def calc(list: List[Int]): List[Int] = {
    if (!list.isEmpty) {
      val processResult = process(list.head)
      val tail = list.tail
      processResult match {
        case Success(calcNum) => calcNum :: calc(tail)
        case _ => Nil
      }
    } else {
      Nil
    }
  }

  def process(num: Int): Try[Int] = {
    Try {
      if (num == 30)
        throw new Exception("oops")
      else num + 1
    }
  }
}
