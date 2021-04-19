package com.pagero.cats

import java.util.concurrent.TimeUnit

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.data.OptionT
import cats.implicits._

import scala.concurrent.duration.Duration

object CatsOptionT extends App {

  def example(): Unit = {
    val greetingFO: Future[Option[String]] = Future.successful(Some("Hello"))
    val firstNameF: Future[String] = Future.successful("Kanishka")
    val lastNameO: Option[String] = Some("Dilshan")
//    val addressOptionF : Option[Future[String]] = Some(Future.successful("Galle"))
    val addressOptionF : Option[Future[String]] = None

    val ot: OptionT[Future, String] = for {
      g <- OptionT(greetingFO)
      f <- OptionT.liftF(firstNameF)
      l <- OptionT.fromOption[Future](lastNameO)
      addrOpTF <- OptionT.fromOption[Future](addressOptionF)
      a <- OptionT.liftF(addrOpTF)
    } yield s"Name : $g $f $l, Address : $a"

    val result = ot.value

    val res = Await.result(result, Duration(1, TimeUnit.HOURS))

    println(res)
  }

  example()
}
